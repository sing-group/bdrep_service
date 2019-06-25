package org.datasetservice;

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
        String url = chargeProperty("url");
        String user = chargeProperty("user");
        String password = chargeProperty("password");
        String datasetStorage = chargeProperty("datasetStorage");
        String pipelineStorage = chargeProperty("pipelineStorage");
        String outputStorage = chargeProperty("outputStorage");
        
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
    private static String chargeProperty(String key) throws Exception
    {
        Properties properties = new Properties();
        String toRet = "";

        properties.load(new FileInputStream("service.properties"));
        toRet = properties.getProperty(key);

        return toRet;
    }
}