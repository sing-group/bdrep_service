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
import org.datasetservice.domain.Language;
import org.datasetservice.domain.License;
import org.datasetservice.domain.Task;
import org.datasetservice.domain.TaskCreateSdataset;
import org.datasetservice.domain.TaskCreateUdataset;

public class TaskCreateUdatasetDAO {

    private String url;

    private String user;

    private String password;

    public TaskCreateUdatasetDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;

    }

    public ArrayList<TaskCreateUdataset> getWaitingUserTasks()
    {
        ArrayList<TaskCreateUdataset> waitingUtasks = new ArrayList<TaskCreateUdataset>();

        String query = "select t.id, t.message, t.state, ud.limit_ham_percentage_eml,"+
        "ud.limit_spam_percentage_eml, ud.limit_ham_percentage_twtid, ud.limit_spam_percentage_twtid,"+
        "ud.limit_ham_percentage_tsms, ud.limit_spam_percentage_tsms,ud.limit_ham_percentage_tytb,"+
        "ud.limit_spam_percentage_tytb, ud.limit_ham_percentage_warc, ud.limit_spam_percentage_warc,"+
        "ud.limit_number_of_files, ud.limit_percentage_spam, ud.spam_mode, ud.date_to, ud.date_from "
        + "from task_create_udataset ud inner join task t on t.id = ud.id where t.state='waiting'";
        try(Connection connection = DriverManager.getConnection(url, user, password);
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query)
        )
        {

            while(rs.next())
            {
                DatasetDAO datasetDAO = new DatasetDAO(url, user, password);
                Dataset dataset = datasetDAO.getDatasetByTaskId(rs.getLong(1));

                if(dataset != null)
                {
                    Long taskId = rs.getLong(1);
                    String message = rs.getString(2);
                    String state = rs.getString(3);
                    int limitPercentageSpam = rs.getInt(15);
                    int limitNumberOfFiles = rs.getInt(14);
                    Date dateFrom = rs.getDate(18);
                    Date dateTo = rs.getDate(17);

                    LanguageDAO languageDAO = new LanguageDAO(url, user, password);
                    LicenseDAO licenseDAO = new LicenseDAO(url, user, password);
                    DatatypeDAO datatypeDAO = new DatatypeDAO(url, user, password);

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

                    //TODO: Delete this test prints
                    /**
                     * for(Language language : languages)
                    {
                        System.out.println(language.getLanguage());
                    }

                    for(License license : licenses)
                    {
                        System.out.println(license.getName());
                    }

                    for(Datatype datatype : datatypes)
                    {
                        System.out.println(datatype.getDatatype());
                    }

                    for(Dataset datasetName : datasets)
                    {
                        System.out.println(datasetName.getName());
                    }
                     */

                    TaskCreateUdataset taskCreateUdataset = new TaskCreateUdataset(taskId, dataset, state, message, limitPercentageSpam, limitNumberOfFiles,
                     dateFrom, dateTo, languages, datatypes, licenses, datasets, limitSpamPercentageEml, limitHamPercentageEml, limitSpamPercentageWarc,
                      limitHamPercentageWarc, limitSpamPercentageTytb, limitHamPercentageTytb, limitSpamPercentageTsms, limitHamPercentageTsms, limitSpamPercentageTwtid,
                       limitHamPercentageTwtid, spamMode);

                    waitingUtasks.add(taskCreateUdataset);
                }
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

        return waitingUtasks;
    }

    public void stablishLicense(TaskCreateUdataset task)
    {
        String query = "select l.name, l.restriction_level from license l inner join dataset d on l.name=d.id "+
         "where d.name in (select dt.dataset from task_create_udataset t inner join task_create_udataset_datasets dt on dt.task_id=t.id where t.id=?)";

        String updateQuery = "update dataset set id=? where task_id=?";

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        PreparedStatement preparedStatementUpdate = connection.prepareStatement(updateQuery))
        {
            preparedStatement.setLong(1, task.getId());

            ResultSet rs = preparedStatement.executeQuery();
            int minRestrictionLevel = 1;
            String license = "Public domain";

            while(rs.next())
            {
                int actual = rs.getInt(2);

                if(actual>minRestrictionLevel)
                {
                    license = rs.getString(1);
                }
            }

            preparedStatementUpdate.setString(1, license);
            preparedStatementUpdate.setLong(2, task.getId());

            preparedStatementUpdate.executeUpdate();

        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

    }
    
}