package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.datasetservice.domain.Dataset;
import org.datasetservice.domain.File;

public class DatasetDAO {

    private String url;

    private String user;

    private String password;

    public DatasetDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;

    }

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

    //TODO: Implement this method
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