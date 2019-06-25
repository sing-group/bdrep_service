package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.datasetservice.domain.Dataset;
import org.datasetservice.domain.Datatype;
import org.datasetservice.domain.File;
import org.datasetservice.domain.Language;
import org.datasetservice.domain.TaskCreateUdataset;

/**
 * Data access object for File 
 */
public class FileDAO {

    /**
     * The connection url to the database
     */
    private String url;

    /**
     * The user of the database
     */
    private String user;

    /**
     * The password for the database
     */
    private String password;

    /**
     * A constructor for create instances of DatasetDAO
     * @param url The connection url to the database
     * @param user The user of the database
     * @param password The password for the database
     */
    public FileDAO(String url, String user, String password)
    {

        this.url = url;
        this.user = user;
        this.password = password;

    }

    /**
     * Insert the specified file in the specified dataset
     * @param file the file to insert 
     * @param dataset the dataset 
     */
    public void insertDatasetFile(File file, Dataset dataset) 
    {
        Date date = file.getDate();
        String extension = file.getExtension();
        String language = file.getLanguage();
        String path = file.getPath();
        String type = file.getType();

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into file(date, extension, language, path, type) values(?, ?, ?, ?, ?)"
        , Statement.RETURN_GENERATED_KEYS);
        PreparedStatement preparedStatement2 = connection.prepareStatement("insert into dataset_files(dataset_name, file_id) values(?, ?)");)
        {
            connection.setAutoCommit(false);
            if(date!=null)
            {
                preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            }
            else
            {
                preparedStatement.setDate(1, null);
            }
            
            preparedStatement.setString(2, extension);
            preparedStatement.setString(3, language);
            preparedStatement.setString(4, path);
            preparedStatement.setString(5, type);

            int rows = preparedStatement.executeUpdate();

            if(rows == 0)
            {
                throw new SQLException();
            }

            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys())
            {
                if(generatedKeys.next())
                {
                    Long fileId = generatedKeys.getLong(1);

                    preparedStatement2.setString(1, dataset.getName());
                    preparedStatement2.setLong(2, fileId);

                    int rows2 = preparedStatement2.executeUpdate();

                    this.insertLanguageFile(dataset.getName(), language);
                    this.insertDatatypeFile(dataset.getName(), extension);
                    

                    if(rows2 == 0)
                        throw new SQLException();
                    else
                    {
                        connection.commit();
                    }

                }
                else
                {
                    throw new SQLException();
                }
                
            }
        }
        catch(SQLException sqlException)
        {
        }     

    }

    /**
     * Insert the specified file to the specified dataset
     * @param file the specified file
     * @param dataset the specified dataset
     */
    public void insertFileById(File file, Dataset dataset)
    {
        Long id = file.getId();
        String datasetName = dataset.getName();

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into dataset_files(dataset_name, file_id) values(?, ?)");
        PreparedStatement preparedStatement2 = connection.prepareStatement("select * from file where id = ?")
        )
        {
            preparedStatement.setString(1, datasetName);
            preparedStatement.setLong(2, id);

            int rows = preparedStatement.executeUpdate();

            if(rows==0)
            {
                throw new SQLException();
            }

            preparedStatement2.setLong(1, id);

            ResultSet rs = preparedStatement2.executeQuery();
            if(rs.next())
            {
                this.insertLanguageFile(datasetName, rs.getString("language"));
                this.insertDatatypeFile(datasetName, rs.getString("extension"));
            }

        }
        catch(SQLException sqlException)
        {
            
        }
    }

    /**
     * Return a list of files associated to the specified dataset
     * @param datasetName the name of the dataset
     * @return a list of files associated to the specified dataset
     */
    public ArrayList<File> getDatasetFiles(String datasetName)
    {

        ArrayList<File> datasetFiles = new ArrayList<File>();
        String query = "select date, extension, language, path, type from file f inner join dataset_files df on f.id=df.file_id where df.dataset_name=?";

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement(query);)
        {
            preparedStatement.setString(1, datasetName);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                Date date = rs.getDate(1);
                String extension = rs.getString(2);
                String language = rs.getString(3);
                String path = rs.getString(4);
                String type = rs.getString(5);

                File file = new File(path, type, language, date, extension);
                datasetFiles.add(file);
            }
        }
        catch(SQLException sqlException)
        {

        }
        
        return datasetFiles;
    }

    /**
     * Return a list of random files
     * @param task the user task 
     * @param spamMode the creation mode
     * @return a list of random files
     */
    public ArrayList<File> getRandomFiles(TaskCreateUdataset task, boolean spamMode)
    {
        ArrayList<File> files = new ArrayList<File>();
        int fileLimit = task.getLimitNumberOfFiles();

        String query = createQueryRandomFiles(task, spamMode);

        if(spamMode)
        {
            int necesarySpam = (int) Math.ceil((double) fileLimit * ((double) task.getLimitPercentageSpam()/100.00));
            int necesaryHam = fileLimit - necesarySpam;

            try(Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(query))
            {
                preparedStatement.setString(1, "spam");
                preparedStatement.setInt(2, necesarySpam);

                ResultSet rs1 = preparedStatement.executeQuery();

                while(rs1.next())
                {
                    System.out.println(rs1.getLong("id") + "\t" +rs1.getDate("date")+ "\t" + rs1.getString("path") + "\t" + rs1.getString("extension")+ "\t" +rs1.getString("type") );
                    File file = new File(rs1.getLong("id"), rs1.getString("path"), rs1.getString("type"), rs1.getString("language"), rs1.getDate("date"), rs1.getString("extension"));
                    files.add(file);
                }

                preparedStatement.setString(1, "ham");
                preparedStatement.setInt(2, necesaryHam);

                ResultSet rs2 = preparedStatement.executeQuery();

                while(rs2.next())
                {
                    System.out.println(rs2.getLong("id") + "\t" +rs2.getDate("date")+ "\t" + rs2.getString("path") + "\t" + rs2.getString("extension")+ "\t" +rs2.getString("type") );
                    File file = new File(rs2.getLong("id"), rs2.getString("path"), rs2.getString("type"), rs2.getString("language"), rs2.getDate("date"), rs2.getString("extension"));
                    files.add(file); 
                }

            }
            catch(SQLException sqlException)
            {
                sqlException.printStackTrace();
            }
        }
        else
        {

        int necesaryHamEml = (int) Math.ceil((double) fileLimit * ((double)task.getLimitHamPercentageEml()/100.00));
        int necesaryHamTwtid = (int) Math.ceil((double)fileLimit * ((double)task.getLimitHamPercentageTwtid()/100.00));
        int necesaryHamTsms = (int) Math.ceil((double)fileLimit * ((double)task.getLimitHamPercentageTsms()/100.00));
        int necesaryHamTytb = (int) Math.ceil((double)fileLimit * ((double)task.getLimitHamPercentageTytb()/100.00));
        int necesaryHamWarc = (int) Math.ceil((double)fileLimit * ((double)task.getLimitHamPercentageWarc()/100.00));

        int necesarySpamEml = (int) Math.ceil((double)fileLimit * ((double)task.getLimitSpamPercentageEml()/100.00));
        int necesarySpamTwtid = (int) Math.ceil((double)fileLimit * ((double)task.getLimitSpamPercentageTwtid()/100.00));
        int necesarySpamTsms = (int) Math.ceil((double)fileLimit * ((double)task.getLimitSpamPercentageTsms()/100.00));
        int necesarySpamTytb = (int) Math.ceil((double)fileLimit * ((double)task.getLimitSpamPercentageTytb()/100.00));
        int necesarySpamWarc = (int) Math.ceil((double)fileLimit * ((double)task.getLimitSpamPercentageWarc()/100.00));

        String[] types = new String[]{"ham", "ham", "ham", "ham", "ham", "spam", "spam", "spam", "spam", "spam"};
        String[] extensions = new String[]{".eml", ".twtid", ".tsms", ".tytb", ".warc", ".eml", ".twtid", ".tsms", ".tytb", ".warc"};
        int[] necesaryFiles = new int[]{necesaryHamEml, necesaryHamTwtid, necesaryHamTsms, necesaryHamTytb, necesaryHamWarc, necesarySpamEml, necesarySpamTwtid,
            necesarySpamTsms, necesarySpamTytb, necesarySpamWarc};

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement(query))
         {
            for(int i = 0; i<types.length;i++)
            {
                preparedStatement.setString(1, extensions[i]);
                preparedStatement.setString(2, types[i]);
                preparedStatement.setInt(3, necesaryFiles[i]);

                ResultSet rs = preparedStatement.executeQuery();

                while(rs.next())
                {
                    System.out.println(rs.getLong("id") + "\t" +rs.getDate("date")+ "\t" + rs.getString("path") + "\t" + rs.getString("extension")+ "\t" +rs.getString("type") );
                    File file = new File(rs.getLong("id"), rs.getString("path"), rs.getString("type"), rs.getString("language"), rs.getDate("date"), rs.getString("extension"));
                    files.add(file);
                }
            }
         }
         catch(SQLException sqlException)
         {
             sqlException.printStackTrace();
         }
        }

        return files;
    }

    /**
     * Check if the task is posible after apply filters
     * @param task the specified task
     * @return true if posible, false if not posible
     */
    public boolean posibleAfterFilters(TaskCreateUdataset task)
    {
        boolean posible = true;

        int necesaryHamEml = (int) Math.ceil((double)task.getLimitNumberOfFiles() * ((double)task.getLimitHamPercentageEml()/100.00));
        int necesaryHamTwtid = (int) Math.ceil((double)task.getLimitNumberOfFiles() * ((double)task.getLimitHamPercentageTwtid()/100.00));
        int necesaryHamTsms = (int) Math.ceil((double)task.getLimitNumberOfFiles() * ((double)task.getLimitHamPercentageTsms()/100.00));
        int necesaryHamTytb = (int) Math.ceil((double)task.getLimitNumberOfFiles() * ((double)task.getLimitHamPercentageTytb()/100.00));
        int necesaryHamWarc = (int) Math.ceil((double)task.getLimitNumberOfFiles() * ((double)task.getLimitHamPercentageWarc()/100.00));

        int necesarySpamEml = (int) Math.ceil((double)task.getLimitNumberOfFiles() * ((double)task.getLimitSpamPercentageEml()/100.00));
        int necesarySpamTwtid = (int) Math.ceil((double)task.getLimitNumberOfFiles() * ((double)task.getLimitSpamPercentageTwtid()/100.00));
        int necesarySpamTsms = (int) Math.ceil((double)task.getLimitNumberOfFiles() * ((double)task.getLimitSpamPercentageTsms()/100.00));
        int necesarySpamTytb = (int) Math.ceil((double)task.getLimitNumberOfFiles() * ((double)task.getLimitSpamPercentageTytb()/100.00));
        int necesarySpamWarc = (int) Math.ceil((double)task.getLimitNumberOfFiles() * ((double)task.getLimitSpamPercentageWarc()/100.00));

        String[] types = new String[]{"ham", "ham", "ham", "ham", "ham", "spam", "spam", "spam", "spam", "spam"};
        String[] extensions = new String[]{".eml", ".twtid", ".tsms", ".tytb", ".warc", ".eml", ".twtid", ".tsms", ".tytb", ".warc"};
        int[] necesaryFiles = new int[]{necesaryHamEml, necesaryHamTwtid, necesaryHamTsms, necesaryHamTytb, necesaryHamWarc, necesarySpamEml, necesarySpamTwtid,
            necesarySpamTsms, necesarySpamTytb, necesarySpamWarc};

        for(int i = 0; i<types.length;i++)
        {
            if(necesaryFiles[i]!=0 && !sufficientFiles(task, extensions[i], types[i], necesaryFiles[i]))
            {
                posible = false;
                break;
            }
        }

        return posible;
    }

    /**
     * Return true if sufficiente files, false if not
     * @param task the user task
     * @param extension the extension
     * @param type the type
     * @param necesary the necesary files
     * @return true if sufficiente files, false if not
     */
    private boolean sufficientFiles(TaskCreateUdataset task, String extension, String type, int necesary)
    {
        String query = createQuery(task, extension, type, necesary);
        boolean success = true;

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1, extension);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, necesary);

            ResultSet rs = preparedStatement.executeQuery();
            if(!rs.first())
            {
                success = false;
            }
        }
        catch(SQLException sqlException)
        {
            return false;
        }

        return success;
    }

    /**
     * Dynamically generates a query for random files
     * @param task the specified task
     * @param spamMode the creation mode
     * @return a query for random files
     */
    private String createQueryRandomFiles(TaskCreateUdataset task, boolean spamMode)
    {
        Long id = task.getId();
        ArrayList<Language> languages = new ArrayList<Language>(task.getLanguages());
        ArrayList<Datatype> datatypes = new ArrayList<Datatype>(task.getDatatypes());

        Date dateFrom = task.getDateFrom();
        Date dateTo = task.getDateTo();

        String query = "select * from file "+
        "f inner join dataset_files df on f.id=df.file_id where df.dataset_name in (select datasets.dataset "+
        "from task_create_udataset task inner join task_create_udataset_datasets datasets on task.id=datasets.task_id where task.id="+id+") ";

        if(!languages.isEmpty())
        {
            query+= "and f.language in (select l.language from task_create_udataset t inner join taskcreateudataset_languages l on t.id=l.task_id where l.task_id="+id+") ";
        }

        if(!datatypes.isEmpty())
        {
            query+= "and f.extension in(select d.datatype from task_create_udataset t inner join taskcreateudataset_datatypes d on t.id=d.task_id where d.task_id="+id+") ";
        }

        if(dateFrom!=null && dateTo!=null)
        {
            query+= "and f.date between (select date_from from task_create_udataset where id="+id+") and (select date_to from task_create_udataset where id="+id+")";
        }

        if(!spamMode)
            query += "and f.extension=? and f.type=? order by RAND() limit ?";
        else
            query += "and f.type=? order by RAND() limit ?";
        
        System.out.println(query);
        return query;
    }

    /**
     * Create a query for specified user task
     * @param task the specified task
     * @param extension the specified extension
     * @param type the specified type
     * @param necesary the necesary files
     * @return a query for specified user task
     */
    private String createQuery(TaskCreateUdataset task, String extension, String type, int necesary)
    {
        Long id = task.getId();
        ArrayList<Language> languages = new ArrayList<Language>(task.getLanguages());
        ArrayList<Datatype> datatypes = new ArrayList<Datatype>(task.getDatatypes());

        Date dateFrom = task.getDateFrom();
        Date dateTo = task.getDateTo();

        String query = "select f.extension, f.type, count(*) as count from file "+
        "f inner join dataset_files df on f.id=df.file_id where df.dataset_name in (select datasets.dataset "+
        "from task_create_udataset task inner join task_create_udataset_datasets datasets on task.id=datasets.task_id where task.id="+id+") ";

        if(!languages.isEmpty())
        {
            query+= "and f.language in (select l.language from task_create_udataset t inner join taskcreateudataset_languages l on t.id=l.task_id where l.task_id="+id+") ";
        }

        if(!datatypes.isEmpty())
        {
            query+= "and f.extension in(select d.datatype from task_create_udataset t inner join taskcreateudataset_datatypes d on t.id=d.task_id where d.task_id="+id+") ";
        }

        if(dateFrom!=null && dateTo!=null)
        {
            query+= "and f.date between (select date_from from task_create_udataset where id="+id+") and (select date_to from task_create_udataset where id="+id+")";
        }

            query += "group by f.extension, f.type having (f.extension=? and f.type=? and count>=?)";

        return query;

    }

    /**
     * Insert a language for the dataset
     * @param datasetName the name of the dataset
     * @param language the language
     */
    private void insertLanguageFile(String datasetName, String language)
    {
        System.out.println("----------------------------------------------------------------");
        System.out.println(language);
        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into dataset_languages(dataset_name, language) values(?, ?)");)
        {
            preparedStatement.setString(1, datasetName);
            preparedStatement.setString(2, language);

            preparedStatement.executeUpdate();
        }
        catch(SQLException sqlException)
        {

        }
    }

    /**
     * Insert a datatype for the dataset
     * @param datasetName the dataset name
     * @param datatype the datatype
     */
    private void insertDatatypeFile(String datasetName, String datatype)
    {
        System.out.println("----------------------------------------------------------------");
        System.out.println(datatype);
        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into dataset_datatypes(dataset_name, data_type) values(?, ?)"))
        {
            preparedStatement.setString(1, datasetName);
            preparedStatement.setString(2, datatype);

            preparedStatement.executeUpdate();
        }
        catch(SQLException sqlException)
        {

        }
    }
    
}