package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.datasetservice.domain.Dataset;

/**
 * Data access object for Dataset 
 */
public class DatasetDAO {

    /**
     * The connection url to the database
     */
    private String url;

    /**
     * The user of the database
     */
    private String user;

    /**
     * The password for the database
     */
    private String password;

    /**
     * A constructor for create instances of DatasetDAO
     * @param url The connection url to the database
     * @param user The user of the database
     * @param password The password for the database
     */
    public DatasetDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;

    }

    /**
     * Return dataset associated to the specified task
     * @param id the id of the task
     * @return dataset associated to the specified task
     */
    public Dataset getDatasetByTaskId(Long id) {

        Dataset dataset = new Dataset();

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from dataset where task_id=?");
        )
        {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next())
            {
                dataset.setName(rs.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            System.out.println(sqlException.toString());
        }

        return dataset;
    }

    
    /**
     * Return the datasets associated to the specified task
     * @param id the id of the task
     * @return a list of the datasets associated to the specified task
     */
    public ArrayList<Dataset> getDatasetsUserTask(Long id)
    {
        ArrayList<Dataset> datasets = new ArrayList<Dataset>();

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("select dataset from task_create_udataset_datasets where task_id=?"))
        {

            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                String datasetName = rs.getString(1);

                Dataset dataset = new Dataset(datasetName);
                datasets.add(dataset);
            }

        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

        return datasets;
    }

    /**
     * Stablish true the specified dataset
     * @param name the name of the dataset
     * @param available the state of the dataset
     */
    public void setAvailable(String name, boolean available)
    {

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("update dataset set available=? where name=?");)
        {
            preparedStatement.setBoolean(1, available);
            preparedStatement.setString(2, name);

            preparedStatement.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            System.out.println(sqlException.toString());
        }

    }

    /**
     * Complete the fields of the specified dataset
     * @param datasetName the name of the dataset
     * @param spamPercentage the spam percentage of the dataset
     * @param hamPercentage the ham percentage of the dataset
     * @param dateFrom the initial date of the messages of the dataset
     * @param dateTo the final date of the messages of the dataset
     */
    public void completeFields(String datasetName, int spamPercentage, int hamPercentage, Date dateFrom, Date dateTo)
    {

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("update dataset set spam_percentage = ?, ham_percentage = ?, first_file_date = ?, last_file_date = ? where name = ?"))
        {
            preparedStatement.setInt(1, spamPercentage);
            preparedStatement.setInt(2, hamPercentage);

            if(dateFrom!=null && dateTo!=null)
            {
                preparedStatement.setDate(3, new java.sql.Date(dateFrom.getTime()));
                preparedStatement.setDate(4, new java.sql.Date(dateTo.getTime()));
            }
            else
            {
                preparedStatement.setDate(3, null);
                preparedStatement.setDate(4, null);
            }

            preparedStatement.setString(5, datasetName);

            preparedStatement.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            
        }

    }
    
}