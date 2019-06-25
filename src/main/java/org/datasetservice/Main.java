package org.datasetservice;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.datasetservice.dao.TaskCreateSdatasetDAO;
import org.datasetservice.dao.TaskCreateUPreprocessingDAO;
import org.datasetservice.dao.TaskCreateUdatasetDAO;
import org.datasetservice.domain.TaskCreateSdataset;
import org.datasetservice.domain.TaskCreateUPreprocessing;
import org.datasetservice.domain.TaskCreateUdataset;
import org.datasetservice.preprocessor.Preprocessor;

public class Main
{

    public static void main(String[] args) throws Exception
    {
        Main main = new Main();

        String url = main.chargeProperty("url");
        String user = main.chargeProperty("user");
        String password = main.chargeProperty("password");
        String datasetStorage = main.chargeProperty("datasetStorage");
        String pipelineStorage = main.chargeProperty("pipelineStorage");
        String outputStorage = main.chargeProperty("outputStorage");
        
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

    /**
     * Take the specified property of the properties file
     * @param key the key of the property
     * @return a String with the value of the property
     * @throws Exception
     */
    private String chargeProperty(String key) throws Exception
    {
        Properties properties = new Properties();
        String toRet = "";

        ClassLoader classLoader = getClass().getClassLoader();
        properties.load(classLoader.getResourceAsStream("service.properties"));
        toRet = properties.getProperty(key);

        return toRet;
    }

}