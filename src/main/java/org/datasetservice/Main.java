package org.datasetservice;

import java.util.ArrayList;
import java.util.Properties;

import org.datasetservice.dao.TaskCreateSdatasetDAO;
import org.datasetservice.dao.TaskCreateUPreprocessingDAO;
import org.datasetservice.dao.TaskCreateUdatasetDAO;
import org.datasetservice.dao.ConnectionPool;
import org.datasetservice.domain.TaskCreateSdataset;
import org.datasetservice.domain.TaskCreateUPreprocessing;
import org.datasetservice.domain.TaskCreateUdataset;
import org.datasetservice.preprocessor.Preprocessor;

import org.apache.commons.dbcp2.BasicDataSource;

public class Main {

    static final long CHECK_INTERVAL_MS = 5000;

    public static void main(String[] args) throws Exception {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUsername(getProperty("user"));
        ds.setPassword(getProperty("password"));

        String datasetStorage = getProperty("datasetStorage");
        String pipelineStorage = getProperty("pipelineStorage");
        String outputStorage = getProperty("outputStorage");

        Preprocessor preprocessor = new Preprocessor(datasetStorage, pipelineStorage, outputStorage);

        TaskCreateSdatasetDAO taskCreateSdatasetDAO = new TaskCreateSdatasetDAO();
        TaskCreateUdatasetDAO taskCreateUdatasetDAO = new TaskCreateUdatasetDAO();
        TaskCreateUPreprocessingDAO taskCreateUPreprocessingDAO = new TaskCreateUPreprocessingDAO();

        System.out.println("Waiting for tasks");

        Long startTime = System.currentTimeMillis();
        while (true) {
            Thread.sleep(CHECK_INTERVAL_MS - (System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();
            System.out.println(ConnectionPool.getDataSource().getNumActive() + "/" + ConnectionPool.getDataSource().getNumIdle());

            ArrayList<TaskCreateSdataset> waitingSTasks = taskCreateSdatasetDAO.getWaitingSystemTasks();

            for (TaskCreateSdataset task : waitingSTasks) {
                String datasetName = task.getDataset().getName();
                if (datasetName != null) {
                    preprocessor.preprocessSystemTask(task);
                }
            }

            ArrayList<TaskCreateUdataset> waitingUTasks = taskCreateUdatasetDAO.getWaitingUserTasks();

            waitingUTasks.forEach((task) -> {
                String datasetName = task.getDataset().getName();
                if (datasetName != null) {
                    preprocessor.preprocessUserTask(task);
                }
            });

            ArrayList<TaskCreateUPreprocessing> waitingPreprocessingTasks = taskCreateUPreprocessingDAO.getWaitingTasksCreateUPreprocessingDAO();

            waitingPreprocessingTasks.forEach((task) -> {
                preprocessor.preprocessDataset(task);
            });
        }
    }

    /**
     * Take the specified property of the properties file
     *
     * @param key the key of the property
     * @return a String with the value of the property
     * @throws Exception
     */
    public static String getProperty(String key) throws Exception {
        Properties properties = new Properties();
        String toRet = "";

        properties.load(Main.class.getClassLoader().getResourceAsStream("service.properties"));
        toRet = properties.getProperty(key);

        return toRet;
    }

}
