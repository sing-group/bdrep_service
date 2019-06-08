package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.datasetservice.domain.Dataset;
import org.datasetservice.domain.Task;
import org.datasetservice.domain.TaskCreateSdataset;

public class TaskCreateSdatasetDAO {

    private final String URL = "jdbc:mysql://localhost:3306/onlinepreprocessor";

    private final String USER = "springuser";

    private final String PASSWORD = "springpassword";

    private DatasetDAO datasetDAO;

    private TaskDAO taskDAO;

    public TaskCreateSdatasetDAO(DatasetDAO datasetDAO, TaskDAO taskDAO)
    {
        this.datasetDAO = datasetDAO;
        this.taskDAO = taskDAO;
    }

    //TODO: Rehacer esto con un join entre task, task_create_sdataset y dataset
    public ArrayList<TaskCreateSdataset> getWaitingSystemTasks() throws Exception 
    {
        ArrayList<TaskCreateSdataset> waitingSystemTasks = new ArrayList<TaskCreateSdataset>();

        String query = "select t.id, t.message, t.state from task_create_sdataset sd inner join task t on t.id = sd.id where t.state='waiting'";
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query)
        )
        {

            while(rs.next())
            {
                Dataset dataset = datasetDAO.getDatasetByTaskId(rs.getLong(1));

                if(dataset != null)
                {
                    TaskCreateSdataset taskCreateSdataset = new TaskCreateSdataset(rs.getLong(1), dataset, rs.getString(2), rs.getString(3));
                    waitingSystemTasks.add(taskCreateSdataset);
                }
            }
        }

        return waitingSystemTasks;
    }
}