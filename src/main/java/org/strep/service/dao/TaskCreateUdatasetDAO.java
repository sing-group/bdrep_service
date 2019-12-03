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
import org.strep.service.domain.Datatype;
import org.strep.service.domain.Language;
import org.strep.service.domain.License;
import org.strep.service.domain.TaskCreateUdataset;

/**
 * The user tasks data access object
 */
public class TaskCreateUdatasetDAO {

    /**
     * The connection url to the database
     */
    private static final Logger logger = LogManager.getLogger(TaskCreateUdatasetDAO.class);

    /**
     * A constructor for create instances of TaskCreateUDatasetDAO
     */
    public TaskCreateUdatasetDAO() {
    }

    /**
     * Return a list of the waiting user tasks
     *
     * @return a list of the waiting user tasks
     */
    public ArrayList<TaskCreateUdataset> getWaitingUserTasks() {
        ArrayList<TaskCreateUdataset> waitingUtasks = new ArrayList<>();

        String query = "select t.id, t.message, t.state, ud.limit_ham_percentage_eml,"
                + "ud.limit_spam_percentage_eml, ud.limit_ham_percentage_twtid, ud.limit_spam_percentage_twtid,"
                + "ud.limit_ham_percentage_tsms, ud.limit_spam_percentage_tsms,ud.limit_ham_percentage_ytbid,"
                + "ud.limit_spam_percentage_ytbid, ud.limit_ham_percentage_warc, ud.limit_spam_percentage_warc,"
                + "ud.limit_number_of_files, ud.limit_percentage_spam, ud.spam_mode, ud.date_to, ud.date_from "
                + "from task_create_udataset ud inner join task t on t.id = ud.id where t.state='waiting'";
        try (Connection connection = ConnectionPool.getDataSourceConnection();
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                DatasetDAO datasetDAO = new DatasetDAO();
                Dataset dataset = datasetDAO.getDatasetByTaskId(rs.getLong(1));

                if (dataset != null) {
                    Long taskId = rs.getLong(1);
                    String message = rs.getString(2);
                    String state = rs.getString(3);
                    int limitPercentageSpam = rs.getInt(15);
                    int limitNumberOfFiles = rs.getInt(14);
                    Date dateFrom = rs.getDate(18);
                    Date dateTo = rs.getDate(17);

                    LanguageDAO languageDAO = new LanguageDAO();
                    LicenseDAO licenseDAO = new LicenseDAO();
                    DatatypeDAO datatypeDAO = new DatatypeDAO();

                    ArrayList<Language> languages = languageDAO.getLanguages(taskId);
                    ArrayList<License> licenses = licenseDAO.getLicenses(taskId);
                    ArrayList<Datatype> datatypes = datatypeDAO.getDatatypes(taskId);
                    ArrayList<Dataset> datasets = datasetDAO.getDatasetsUserTask(taskId);

                    int limitSpamPercentageEml = rs.getInt(5);
                    int limitHamPercentageEml = rs.getInt(4);

                    int limitSpamPercentageWarc = rs.getInt(13);
                    int limitHamPercentageWarc = rs.getInt(12);

                    int limitSpamPercentageTytb = rs.getInt(11);
                    int limitHamPercentageTytb = rs.getInt(10);

                    int limitSpamPercentageTsms = rs.getInt(9);
                    int limitHamPercentageTsms = rs.getInt(8);

                    int limitSpamPercentageTwtid = rs.getInt(7);
                    int limitHamPercentageTwtid = rs.getInt(6);

                    boolean spamMode = rs.getBoolean(16);

                    TaskCreateUdataset taskCreateUdataset = new TaskCreateUdataset(taskId, dataset, state, message, limitPercentageSpam, limitNumberOfFiles,
                            dateFrom, dateTo, languages, datatypes, licenses, datasets, limitSpamPercentageEml, limitHamPercentageEml, limitSpamPercentageWarc,
                            limitHamPercentageWarc, limitSpamPercentageTytb, limitHamPercentageTytb, limitSpamPercentageTsms, limitHamPercentageTsms, limitSpamPercentageTwtid,
                            limitHamPercentageTwtid, spamMode);

                    waitingUtasks.add(taskCreateUdataset);
                }
            }
        } catch (SQLException sqlException) {
           logger.warn("[ERROR getWaitingUserTasks]: " + sqlException.getMessage());
        }

        return waitingUtasks;
    }

    /**
     * Stablish the license of the specified user task
     *
     * @param task the task to stablish the license
     */
    public void stablishLicense(TaskCreateUdataset task) {
        String query = "select l.name, l.restriction_level from license l inner join dataset d on l.name=d.id "
                + "where d.name in (select dt.dataset from task_create_udataset t inner join task_create_udataset_datasets dt on dt.task_id=t.id where t.id=?)";

        //String updateQuery = "update dataset set id=? where task_id=?";
        String updateQuery = "update dataset set id=? where name in (select dataset_name from task where id=?)";

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateQuery)) {
            preparedStatement.setLong(1, task.getId());

            ResultSet rs = preparedStatement.executeQuery();
            int minRestrictionLevel = 1;
            String license = "Public domain";

            while (rs.next()) {
                int actual = rs.getInt(2);

                if (actual > minRestrictionLevel) {
                    license = rs.getString(1);
                }
            }

            preparedStatementUpdate.setString(1, license);
            preparedStatementUpdate.setLong(2, task.getId());

            preparedStatementUpdate.executeUpdate();
            
        } catch (SQLException sqlException) {
            logger.warn("[ERROR stablishLicense]: " + sqlException.getMessage());
        }

    }

}
