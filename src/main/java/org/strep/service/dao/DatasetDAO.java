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
package org.strep.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.strep.service.domain.Dataset;

/**
 * Data access object for Dataset
 */
public class DatasetDAO {

    /**
     * For logging purposes
     */
    private static final Logger logger = LogManager.getLogger(DatasetDAO.class);

    /**
     * A constructor for create instances of DatasetDAO
     */
    public DatasetDAO() {
    }

    /**
     * Return dataset associated to the specified task
     *
     * @param id the id of the task
     * @return dataset associated to the specified task
     */
    public Dataset getDatasetByTaskId(Long id) {

        Dataset dataset = new Dataset();

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                
            //PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from dataset where task_id=?");) {
            PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT dataset.name AS dname FROM task,dataset WHERE task.dataset_name=dataset.name AND task.id=? AND (task.id IN (SELECT id FROM task_create_sdataset) OR task.id IN (SELECT id FROM task_create_udataset))"
            );){
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                dataset.setName(rs.getString("dname"));
            }
        } catch (SQLException sqlException) {
            logger.warn("[ERROR getDatasetByTaskId]: " + sqlException.getMessage());
        }

        return dataset;
    }

    /**
     * Return the datasets associated to the specified task
     *
     * @param id the id of the task
     * @return a list of the datasets associated to the specified task
     */
    public ArrayList<Dataset> getDatasetsUserTask(Long id) {
        ArrayList<Dataset> datasets = new ArrayList<>();

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("select dataset from task_create_udataset_datasets where task_id=?")) {

            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String datasetName = rs.getString(1);
                Dataset dataset = new Dataset(datasetName);
                datasets.add(dataset);
            }
        } catch (SQLException sqlException) {
            logger.warn("[ERROR getDatasetsUserTask]: " + sqlException.getMessage());
        }
        return datasets;
    }

    /**
     * Stablish true the specified dataset
     *
     * @param name the name of the dataset
     * @param available the state of the dataset
     */
    public void setAvailable(String name, boolean available) {

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE dataset SET available=? WHERE name=?");) {
            preparedStatement.setBoolean(1, available);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.warn("[ERROR setAvailable]: " + sqlException.getMessage());
        }
    }

    /**
     * Complete the fields of the specified dataset
     *
     * @param datasetName the name of the dataset
     * @param spamPercentage the spam percentage of the dataset
     * @param hamPercentage the ham percentage of the dataset
     * @param dateFrom the initial date of the messages of the dataset
     * @param dateTo the final date of the messages of the dataset
     */
    public String completeFields(String datasetName, int spamPercentage, int hamPercentage, Date dateFrom, Date dateTo) {

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("update dataset set spam_percentage = ?, ham_percentage = ?, first_file_date = ?, last_file_date = ? where name = ?")) {
            preparedStatement.setInt(1, spamPercentage);
            preparedStatement.setInt(2, hamPercentage);

            if (dateFrom != null && dateTo != null) {
                preparedStatement.setDate(3, new java.sql.Date(dateFrom.getTime()));
                preparedStatement.setDate(4, new java.sql.Date(dateTo.getTime()));
            } else {
                preparedStatement.setDate(3, null);
                preparedStatement.setDate(4, null);
            }

            preparedStatement.setString(5, datasetName);
            preparedStatement.executeUpdate();
            return null;
        } catch (SQLException sqlException) {
            logger.warn("[ERROR completeFields]: " + sqlException.getMessage());
            return sqlException.getMessage();
        }
    }
}
