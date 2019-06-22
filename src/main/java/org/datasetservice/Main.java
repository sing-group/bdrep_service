package org.datasetservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.datasetservice.dao.*;
import org.datasetservice.domain.*;
import org.datasetservice.preprocessor.Preprocessor;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        DatasetDAO datasetDAO = new DatasetDAO();
        TaskDAO taskDAO = new TaskDAO();
        LanguageDAO languageDAO = new LanguageDAO();
        LicenseDAO licenseDAO = new LicenseDAO();
        DatatypeDAO datatypeDAO = new DatatypeDAO();
        
        Preprocessor preprocessor = new Preprocessor("/home/ismael/Desarrollo/datasets");

        TaskCreateSdatasetDAO taskCreateSdatasetDAO = new TaskCreateSdatasetDAO(datasetDAO, taskDAO);
        TaskCreateUdatasetDAO taskCreateUdatasetDAO = new TaskCreateUdatasetDAO(datasetDAO, taskDAO, languageDAO, licenseDAO, datatypeDAO);
        TaskCreateUPreprocessingDAO taskCreateUPreprocessingDAO = new TaskCreateUPreprocessingDAO(datasetDAO, taskDAO);

        while(true)
        {
            ArrayList<TaskCreateSdataset> waitingSTasks = taskCreateSdatasetDAO.getWaitingSystemTasks();

            for(TaskCreateSdataset task : waitingSTasks)
            {
                String datasetName = task.getDataset().getName();
                if(datasetName!=null)
                    preprocessor.preprocessSystemTask(task);
                
            }

            ArrayList<TaskCreateUdataset> waitingUTasks = taskCreateUdatasetDAO.getWaitingUserTasks();

            for(TaskCreateUdataset task : waitingUTasks)
            {
                String datasetName = task.getDataset().getName();
                if(datasetName!=null)
                    preprocessor.preprocessUserTask(task);
            }

            ArrayList<TaskCreateUPreprocessing> waitingPreprocessingTasks = taskCreateUPreprocessingDAO.getWaitingTasksCreateUPreprocessingDAO();

            for(TaskCreateUPreprocessing task : waitingPreprocessingTasks)
            {
                preprocessor.preprocessDataset(task);
            }
        }
        
        
    }
}