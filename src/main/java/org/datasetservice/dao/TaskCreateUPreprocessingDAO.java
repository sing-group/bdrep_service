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
    private final String URL = "jdbc:mysql://localhost:3306/onlinepreprocessor";

    private final String USER = "springuser";

    private final String PASSWORD = "springpassword";

    private DatasetDAO datasetDAO;

    private TaskDAO taskDAO;

    public TaskCreateUPreprocessingDAO(DatasetDAO datasetDAO, TaskDAO taskDAO)
    {
        this.datasetDAO = datasetDAO;
        this.taskDAO = taskDAO;
    }

    public ArrayList<TaskCreateUPreprocessing> getWaitingTasksCreateUPreprocessingDAO()
    {
        ArrayList<TaskCreateUPreprocessing> tasks = new ArrayList<TaskCreateUPreprocessing>();

        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
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