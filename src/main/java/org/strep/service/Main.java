/*-
 * #%L
 * STRep Service
 * %%
 * Copyright (C) 2019 - 2024 SING Group (University of Vigo)
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.strep.service;

import java.util.ArrayList;
import java.util.Properties;

import org.strep.service.dao.TaskCreateSdatasetDAO;
import org.strep.service.dao.TaskCreateUPreprocessingDAO;
import org.strep.service.dao.TaskCreateUdatasetDAO;
import org.strep.service.domain.TaskCreateSdataset;
import org.strep.service.domain.TaskCreateUPreprocessing;
import org.strep.service.domain.TaskCreateUdataset;
import org.strep.service.preprocessor.Preprocessor;


public class Main {

    static final long CHECK_INTERVAL_MS = 5000;

    public static void main(String[] args) throws Exception {

        String datasetStorage = getProperty("datasetStorage");
        String pipelineStorage = getProperty("pipelineStorage");
        String outputStorage = getProperty("outputStorage");

        Preprocessor preprocessor = new Preprocessor(datasetStorage, pipelineStorage, outputStorage);

        TaskCreateSdatasetDAO taskCreateSdatasetDAO = new TaskCreateSdatasetDAO();
        TaskCreateUdatasetDAO taskCreateUdatasetDAO = new TaskCreateUdatasetDAO();
        TaskCreateUPreprocessingDAO taskCreateUPreprocessingDAO = new TaskCreateUPreprocessingDAO();

        System.out.println("[STRep Service] Waiting for tasks...");

        Long startTime = System.currentTimeMillis();
        while (true) {
            if ((CHECK_INTERVAL_MS - (System.currentTimeMillis() - startTime)>10))
                Thread.sleep(CHECK_INTERVAL_MS - (System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();

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
