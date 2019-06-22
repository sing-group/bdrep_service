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

    private String url;

    private String user;

    private String password;

    public TaskCreateSdatasetDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;

    }

    //TODO: Rehacer esto con un join entre task, task_create_sdataset y dataset
    public ArrayList<TaskCreateSdataset> getWaitingSystemTasks() throws Exception 
    {
        ArrayList<TaskCreateSdataset> waitingSystemTasks = new ArrayList<TaskCreateSdataset>();

        String query = "select t.id, t.message, t.state from task_create_sdataset sd inner join task t on t.id = sd.id where t.state='waiting'";
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
                    TaskCreateSdataset taskCreateSdataset = new TaskCreateSdataset(rs.getLong(1), dataset, rs.getString(2), rs.getString(3));
                    waitingSystemTasks.add(taskCreateSdataset);
                }
            }
        }

        return waitingSystemTasks;
    }
}