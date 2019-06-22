package org.datasetservice;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.datasetservice.dao.*;
import org.datasetservice.domain.*;
import org.datasetservice.preprocessor.Preprocessor;

public class Main
{

    public static void main(String[] args) throws Exception
    {
        String url = chargeProperty("url");
        String user = chargeProperty("user");
        String password = chargeProperty("password");
        String datasetStorage = chargeProperty("datasetStorage");
        String pipelineStorage = chargeProperty("pipelineStorage");
        String outputStorage = chargeProperty("outputStorage");

        DatasetDAO datasetDAO = new DatasetDAO(url, user, password);
        TaskDAO taskDAO = new TaskDAO(url, user, password);
        LanguageDAO languageDAO = new LanguageDAO(url, user, password);
        LicenseDAO licenseDAO = new LicenseDAO(url, user, password);
        DatatypeDAO datatypeDAO = new DatatypeDAO(url, user, password);
        
        Preprocessor preprocessor = new Preprocessor(url, user, password, datasetStorage, pipelineStorage, outputStorage);

        TaskCreateSdatasetDAO taskCreateSdatasetDAO = new TaskCreateSdatasetDAO(url, user, password);
        TaskCreateUdatasetDAO taskCreateUdatasetDAO = new TaskCreateUdatasetDAO(url, user, password);
        TaskCreateUPreprocessingDAO taskCreateUPreprocessingDAO = new TaskCreateUPreprocessingDAO(url, user, password);

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

    private static String chargeProperty(String key) throws Exception
    {
        Properties properties = new Properties();
        String toRet = "";

        properties.load(new FileInputStream("service.properties"));
        toRet = properties.getProperty(key);

        return toRet;
    }
}