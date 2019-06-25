package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.datasetservice.domain.Dataset;
import org.datasetservice.domain.TaskCreateUPreprocessing;

/**
 * The preprocessing tasks data access object
 */
public class TaskCreateUPreprocessingDAO
{
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
     * A constructor for create instances of TaskCreateUPreprocessingDAO
     * @param url The connection url to the database
     * @param user The user of the database
     * @param password The password for the database
     */
    public TaskCreateUPreprocessingDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Return the waiting preprocessing tasks
     * @return the waiting preprocessing tasks
     */
    public ArrayList<TaskCreateUPreprocessing> getWaitingTasksCreateUPreprocessingDAO()
    {
        ArrayList<TaskCreateUPreprocessing> tasks = new ArrayList<TaskCreateUPreprocessing>();

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("select t.id, state, description, pipeline, date, preprocess_dataset_name"+
         " from task_createupreprocessing tp inner join task t on tp.id=t.id where t.state='waiting'"))
        {
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                Long id = rs.getLong("id");
                String state = rs.getString("state");
                String description = rs.getString("description");
                byte[] pipeline = rs.getBytes("pipeline");
                String preprocessDatasetName = rs.getString("preprocess_dataset_name");
                Date date = new Date(rs.getDate("date").getTime());

                Dataset dataset = new Dataset(preprocessDatasetName);

                TaskCreateUPreprocessing task = new TaskCreateUPreprocessing(id, null, state, null, description, pipeline, null, date, dataset);
                tasks.add(task);
            }

        }
        catch(SQLException sqlException)
        {
            return tasks;
        }

        return tasks;
        
    }



}