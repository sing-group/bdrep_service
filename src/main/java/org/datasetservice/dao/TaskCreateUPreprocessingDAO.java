package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.datasetservice.domain.Dataset;
import org.datasetservice.domain.TaskCreateUPreprocessing;

/**
 * The preprocessing tasks data access object
 */
public class TaskCreateUPreprocessingDAO {

    /**
     * For logging purposes
     */
    private static final Logger logger = LogManager.getLogger(TaskCreateUPreprocessingDAO.class);

    /**
     * A constructor for create instances of TaskCreateUPreprocessingDAO
     */
    public TaskCreateUPreprocessingDAO() {
    }

    /**
     * Return the waiting preprocessing tasks
     *
     * @return the waiting preprocessing tasks
     */
    public ArrayList<TaskCreateUPreprocessing> getWaitingTasksCreateUPreprocessingDAO() {
        ArrayList<TaskCreateUPreprocessing> tasks = new ArrayList<>();

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("select t.id, state, description, pipeline, date, preprocess_dataset_name"
                        + " from task_createupreprocessing tp inner join task t on tp.id=t.id where t.state='waiting'")) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
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

        } catch (SQLException sqlException) {
            logger.warn("[ERROR]: " + sqlException.getMessage());
            return tasks;
        }
        return tasks;
    }
}
