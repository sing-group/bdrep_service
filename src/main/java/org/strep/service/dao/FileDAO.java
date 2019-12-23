package org.strep.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.strep.service.domain.Dataset;
import org.strep.service.domain.File;
import org.strep.service.domain.TaskCreateUdataset;

import de.scravy.pair.Pair;
import de.scravy.pair.Pairs;

/**
 * Data access object for File
 */
public class FileDAO {

    /**
     * The connection url to the database
     */
    private static final Logger logger = LogManager.getLogger(FileDAO.class);

    /**
     * A constructor for create instances of DatasetDAO
     */
    public FileDAO() {
    }

    /**
     * Insert the specified file in the specified dataset
     *
     * @param file the file to insert
     * @param dataset the dataset
     */
    public void insertDatasetFile(File file, Dataset dataset) {
        Date date = file.getDate();
        String extension = file.getExtension();
        String language = file.getLanguage();
        String path = file.getPath();
        String type = file.getType();

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO file(date, extension, language, path, type) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO dataset_files(dataset_name, file_id) VALUES (?, ?)");) {
            connection.setAutoCommit(false);
            if (date != null) {
                preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            } else {
                preparedStatement.setDate(1, null);
            }

            preparedStatement.setString(2, extension);
            preparedStatement.setString(3, language);
            preparedStatement.setString(4, path);
            preparedStatement.setString(5, type);

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new SQLException();
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long fileId = generatedKeys.getLong(1);

                    preparedStatement2.setString(1, dataset.getName());
                    preparedStatement2.setLong(2, fileId);

                    int rows2 = preparedStatement2.executeUpdate();

                    this.insertLanguageFile(dataset.getName(), language);
                    this.insertDatatypeFile(dataset.getName(), extension);

                    if (rows2 == 0) {
                        throw new SQLException();
                    } else {
                        connection.commit();
                    }
                } else {
                    throw new SQLException();
                }
            }
        } catch (SQLException sqlException) {
            logger.warn("[ERROR insertDatasetFile]: " + sqlException.getMessage());
        }
    }

    /**
     * Insert the specified file to the specified dataset
     *
     * @param file the specified file
     * @param dataset the specified dataset
     */
    public void insertFileById(File file, Dataset dataset) {
        Long id = file.getId();
        String datasetName = dataset.getName();

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO dataset_files(dataset_name, file_id) VALUES (?, ?)");
                PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM file WHERE id = ?")) {
            preparedStatement.setString(1, datasetName);
            preparedStatement.setLong(2, id);

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new SQLException();
            }

            preparedStatement2.setLong(1, id);

            ResultSet rs = preparedStatement2.executeQuery();
            if (rs.next()) {
                this.insertLanguageFile(datasetName, rs.getString("language"));
                this.insertDatatypeFile(datasetName, rs.getString("extension"));
            }

        } catch (SQLException sqlException) {
            logger.warn("[ERROR insertFileById]: " + sqlException.getMessage());
        }
    }

    /**
     * Return a list of files associated to the specified dataset
     *
     * @param datasetName the name of the dataset
     * @return a list of files associated to the specified dataset
     */
    public ArrayList<File> getDatasetFiles(String datasetName) {

        ArrayList<File> datasetFiles = new ArrayList<>();
        String query = "SELECT date, extension, language, path, type FROM file f INNER JOIN dataset_files df ON f.id=df.file_id WHERE df.dataset_name=?";

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, datasetName);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Date date = rs.getDate(1);
                String extension = rs.getString(2);
                String language = rs.getString(3);
                String path = rs.getString(4);
                String type = rs.getString(5);

                File file = new File(path, type, language, date, extension);
                datasetFiles.add(file);
            }
        } catch (SQLException sqlException) {
            logger.warn("[ERROR getDatasetFiles]: " + sqlException.getMessage());
        }

        return datasetFiles;
    }

    /**
     * Return the dates of the eagest and oldest files
     * @param datasetName The name of the dataset
     * @return the dates of the eagest and oldest files
     */
    public Pair<Date, Date> getDatasetEagestAndOldestDate(String datasetName){
        String query = "SELECT min(date) as min, max(date) as max FROM file f INNER JOIN dataset_files df ON f.id=df.file_id WHERE df.dataset_name=?";
        Pair<Date, Date> retVal=null;

        try (Connection connection = ConnectionPool.getDataSourceConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, datasetName);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Date d1=rs.getDate(1);
                if (rs.wasNull()) d1=new Date(Long.MIN_VALUE);

                Date d2=rs.getDate(2);
                if (rs.wasNull()) d2=new Date();
                retVal=Pairs.from(d1, d2);
            }
        } catch (SQLException sqlException) {
            logger.warn("[ERROR getDatasetEagestAndOldestDate]: " + sqlException.getMessage());
        }

        return retVal;
    }

    /**
     * Compute spam percentage of a dataset
     * @param datasetName
     * @return The spam percentage
     */
    public Double computeSpamPercentage(String datasetName){
        String query = "SELECT (COUNT(CASE type WHEN 'spam' THEN 1 ELSE NULL END)/COUNT(1))*100 as spampercentage FROM file f INNER JOIN dataset_files df ON f.id=df.file_id WHERE df.dataset_name=?";
        Double retVal=null;

        try (Connection connection = ConnectionPool.getDataSourceConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, datasetName);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) { 
                retVal=rs.getDouble(1);
                if (rs.wasNull()) retVal=0d;
            }
        } catch (SQLException sqlException) {
            logger.warn("[ERROR computeSpamPercentage]: " + sqlException.getMessage());
        }

        return retVal;
    }

    /**
     * Return a list of random files
     *
     * @param task the user task
     * @param spamMode the creation mode
     * @return a list of random files
     */
    public ArrayList<File> getRandomFiles(TaskCreateUdataset task, boolean spamMode) {
        ArrayList<File> files = new ArrayList<>();
        int fileLimit = task.getLimitNumberOfFiles();

        String query = createQueryRandomFiles(task, spamMode);

        if (spamMode) {
            int necesarySpam = (int) Math.ceil((double) fileLimit * ((double) task.getLimitPercentageSpam() / 100.00));
            int necesaryHam = fileLimit - necesarySpam;

            try (Connection connection = ConnectionPool.getDataSourceConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "spam");
                preparedStatement.setInt(2, necesarySpam);

                ResultSet rs1 = preparedStatement.executeQuery();

                while (rs1.next()) {
                    System.out.println(rs1.getLong("id") + "\t" + rs1.getDate("date") + "\t" + rs1.getString("path") + "\t" + rs1.getString("extension") + "\t" + rs1.getString("type"));
                    File file = new File(rs1.getLong("id"), rs1.getString("path"), rs1.getString("type"), rs1.getString("language"), rs1.getDate("date"), rs1.getString("extension"));
                    files.add(file);
                }

                preparedStatement.setString(1, "ham");
                preparedStatement.setInt(2, necesaryHam);

                ResultSet rs2 = preparedStatement.executeQuery();

                while (rs2.next()) {
                    System.out.println(rs2.getLong("id") + "\t" + rs2.getDate("date") + "\t" + rs2.getString("path") + "\t" + rs2.getString("extension") + "\t" + rs2.getString("type"));
                    File file = new File(rs2.getLong("id"), rs2.getString("path"), rs2.getString("type"), rs2.getString("language"), rs2.getDate("date"), rs2.getString("extension"));
                    files.add(file);
                }

            } catch (SQLException sqlException) {
                logger.warn("[ERROR getRandomFiles]: " + sqlException.getMessage());
            }
        } else {

            int necesaryHamEml = (int) Math.ceil((double) fileLimit * ((double) task.getLimitHamPercentageEml() / 100.00));
            int necesaryHamTwtid = (int) Math.ceil((double) fileLimit * ((double) task.getLimitHamPercentageTwtid() / 100.00));
            int necesaryHamTsms = (int) Math.ceil((double) fileLimit * ((double) task.getLimitHamPercentageTsms() / 100.00));
            int necesaryHamYtbid = (int) Math.ceil((double) fileLimit * ((double) task.getLimitHamPercentageYtbid() / 100.00));
            int necesaryHamWarc = (int) Math.ceil((double) fileLimit * ((double) task.getLimitHamPercentageWarc() / 100.00));

            int necesarySpamEml = (int) Math.ceil((double) fileLimit * ((double) task.getLimitSpamPercentageEml() / 100.00));
            int necesarySpamTwtid = (int) Math.ceil((double) fileLimit * ((double) task.getLimitSpamPercentageTwtid() / 100.00));
            int necesarySpamTsms = (int) Math.ceil((double) fileLimit * ((double) task.getLimitSpamPercentageTsms() / 100.00));
            int necesarySpamYtbid = (int) Math.ceil((double) fileLimit * ((double) task.getLimitSpamPercentageYtbid() / 100.00));
            int necesarySpamWarc = (int) Math.ceil((double) fileLimit * ((double) task.getLimitSpamPercentageWarc() / 100.00));

            String[] types = new String[]{"ham", "ham", "ham", "ham", "ham", "spam", "spam", "spam", "spam", "spam"};
            String[] extensions = new String[]{".eml", ".twtid", ".tsms", ".ytbid", ".warc", ".eml", ".twtid", ".tsms", ".ytbid", ".warc"};
            int[] necesaryFiles = new int[]{necesaryHamEml, necesaryHamTwtid, necesaryHamTsms, necesaryHamYtbid, necesaryHamWarc, necesarySpamEml, necesarySpamTwtid,
                necesarySpamTsms, necesarySpamYtbid, necesarySpamWarc};

            try (Connection connection = ConnectionPool.getDataSourceConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                for (int i = 0; i < types.length; i++) {
                    preparedStatement.setString(1, extensions[i]);
                    preparedStatement.setString(2, types[i]);
                    preparedStatement.setInt(3, necesaryFiles[i]);

                    ResultSet rs = preparedStatement.executeQuery();

                    while (rs.next()) {
                        System.out.println(rs.getLong("id") + "\t" + rs.getDate("date") + "\t" + rs.getString("path") + "\t" + rs.getString("extension") + "\t" + rs.getString("type"));
                        File file = new File(rs.getLong("id"), rs.getString("path"), rs.getString("type"), rs.getString("language"), rs.getDate("date"), rs.getString("extension"));
                        files.add(file);
                    }
                }
            } catch (SQLException sqlException) {
                logger.warn("[ERROR getRandomFiles]: " + sqlException.getMessage());
            }
        }

        return files;
    }

    /**
     * Check if the task is posible after apply filters
     *
     * @param task the specified task
     * @return true if posible, false if not posible
     */
    public boolean posibleAfterFilters(TaskCreateUdataset task) {
        boolean posible = true;

        int necesaryHamEml = (int) Math.ceil((double) task.getLimitNumberOfFiles() * ((double) task.getLimitHamPercentageEml() / 100.00));
        int necesaryHamTwtid = (int) Math.ceil((double) task.getLimitNumberOfFiles() * ((double) task.getLimitHamPercentageTwtid() / 100.00));
        int necesaryHamTsms = (int) Math.ceil((double) task.getLimitNumberOfFiles() * ((double) task.getLimitHamPercentageTsms() / 100.00));
        int necesaryHamYtbid = (int) Math.ceil((double) task.getLimitNumberOfFiles() * ((double) task.getLimitHamPercentageYtbid() / 100.00));
        int necesaryHamWarc = (int) Math.ceil((double) task.getLimitNumberOfFiles() * ((double) task.getLimitHamPercentageWarc() / 100.00));

        int necesarySpamEml = (int) Math.ceil((double) task.getLimitNumberOfFiles() * ((double) task.getLimitSpamPercentageEml() / 100.00));
        int necesarySpamTwtid = (int) Math.ceil((double) task.getLimitNumberOfFiles() * ((double) task.getLimitSpamPercentageTwtid() / 100.00));
        int necesarySpamTsms = (int) Math.ceil((double) task.getLimitNumberOfFiles() * ((double) task.getLimitSpamPercentageTsms() / 100.00));
        int necesarySpamYtbid = (int) Math.ceil((double) task.getLimitNumberOfFiles() * ((double) task.getLimitSpamPercentageYtbid() / 100.00));
        int necesarySpamWarc = (int) Math.ceil((double) task.getLimitNumberOfFiles() * ((double) task.getLimitSpamPercentageWarc() / 100.00));

        String[] types = new String[]{"ham", "ham", "ham", "ham", "ham", "spam", "spam", "spam", "spam", "spam"};
        String[] extensions = new String[]{".eml", ".twtid", ".tsms", ".ytbid", ".warc", ".eml", ".twtid", ".tsms", ".ytbid", ".warc"};
        int[] necesaryFiles = new int[]{necesaryHamEml, necesaryHamTwtid, necesaryHamTsms, necesaryHamYtbid, necesaryHamWarc, necesarySpamEml, necesarySpamTwtid,
            necesarySpamTsms, necesarySpamYtbid, necesarySpamWarc};

        for (int i = 0; i < types.length; i++) {
            if (necesaryFiles[i] != 0 && !sufficientFiles(task, extensions[i], types[i], necesaryFiles[i])) {
                posible = false;
                break;
            }
        }

        return posible;
    }

    /**
     * Return true if sufficiente files, false if not
     *
     * @param task the user task
     * @param extension the extension
     * @param type the type
     * @param necesary the necesary files
     * @return true if sufficiente files, false if not
     */
    private boolean sufficientFiles(TaskCreateUdataset task, String extension, String type, int necesary) {
        String query = createQuery(task, extension, type, necesary);
        boolean success = true;

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, extension);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, necesary);

            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.first()) {
                success = false;
            }
        } catch (SQLException sqlException) {
            logger.warn("[ERROR sufficientFiles]: " + sqlException.getMessage());
            return false;
        }

        return success;
    }

    /**
     * Dynamically generates a query for random files
     *
     * @param task the specified task
     * @param spamMode the creation mode
     * @return a query for random files
     */
    private String createQueryRandomFiles(TaskCreateUdataset task, boolean spamMode) {
        Long id = task.getId();

        String query = "SELECT * FROM file "
                + "f INNER JOIN dataset_files df ON f.id=df.file_id where df.dataset_name IN (SELECT datasets.dataset "
                + "FROM task_create_udataset task INNER JOIN task_create_udataset_datasets datasets ON task.id=datasets.task_id WHERE task.id=" + id + ") "
                + "AND f.language IN (SELECT l.language FROM task_create_udataset t INNER JOIN taskcreateudataset_languages l ON t.id=l.task_id WHERE l.task_id=" + id + ") "
                + "AND f.extension IN (SELECT d.datatype FROM task_create_udataset t INNER JOIN taskcreateudataset_datatypes d ON t.id=d.task_id WHERE d.task_id=" + id + ") "
                + "AND f.date >= (SELECT date_from FROM task_create_udataset WHERE id=" + id + ") AND f.date <= (SELECT date_to FROM task_create_udataset WHERE id=" + id + ") ";

        if (!spamMode) {
            query += "AND f.extension=? AND f.type=? ORDER BY RAND() LIMIT ?";
        } else {
            query += "AND f.type=? ORDER BY RAND() LIMIT ?";
        }

        System.out.println(query);

        return query;
    }

    /**
     * Create a query for specified user task
     *
     * @param task the specified task
     * @param extension the specified extension
     * @param type the specified type
     * @param necesary the necesary files
     * @return a query for specified user task
     */
    private String createQuery(TaskCreateUdataset task, String extension, String type, int necesary) {
        Long id = task.getId();

        String query = "SELECT f.extension, f.type, count(*) AS count FROM file "
                + "f INNER JOIN dataset_files df ON f.id=df.file_id WHERE df.dataset_name IN (SELECT datasets.dataset "
                + "FROM task_create_udataset task INNER JOIN task_create_udataset_datasets datasets ON task.id=datasets.task_id WHERE task.id=" + id + ") "
                + "AND f.language IN (SELECT l.language FROM task_create_udataset t INNER JOIN taskcreateudataset_languages l ON t.id=l.task_id WHERE l.task_id=" + id + ") "
                + "AND f.extension IN (SELECT d.datatype FROM task_create_udataset t INNER JOIN taskcreateudataset_datatypes d ON t.id=d.task_id WHERE d.task_id=" + id + ") "
                + "AND f.date >= (SELECT date_from FROM task_create_udataset WHERE id=" + id + ") AND f.date <= (SELECT date_to FROM task_create_udataset WHERE id=" + id + ") "
                + "GROUP BY f.extension, f.type HAVING (f.extension=? AND f.type=? AND count>=?)";

        System.out.println(query);

        return query;
    }

    /**
     * Insert a language for the dataset
     *
     * @param datasetName the name of the dataset
     * @param language the language
     */
    private void insertLanguageFile(String datasetName, String language) {
        System.out.println(language);
        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT IGNORE INTO dataset_languages(dataset_name, language) VALUES (?, ?)");) {
            preparedStatement.setString(1, datasetName);
            preparedStatement.setString(2, language);

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.warn("[ERROR insertLanguageFile]: " + sqlException.getMessage());
        }
    }

    /**
     * Insert a datatype for the dataset
     *
     * @param datasetName the dataset name
     * @param datatype the datatype
     */
    private void insertDatatypeFile(String datasetName, String datatype) {
        System.out.println(datatype);
        try (Connection connection = ConnectionPool.getDataSourceConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT IGNORE INTO dataset_datatypes(dataset_name, data_type) VALUES (?, ?)")) {
            //PreparedStatement preparedStatement = connection.prepareStatement("insert into dataset_datatypes(dataset_name, data_type) values(?, ?)")) {
            preparedStatement.setString(1, datasetName);
            preparedStatement.setString(2, datatype);

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.warn("ERROR insertDatatypeFile [datasetName:"+datasetName+", datatype: "+datatype + "]" + sqlException.getMessage());
        }
    }
}
