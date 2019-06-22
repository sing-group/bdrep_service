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

public class TaskCreateUPreprocessingDAO
{

    private String url;
    private String user;
    private String password;

    public TaskCreateUPreprocessingDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

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