package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.datasetservice.domain.License;

/**
 * Data access object for Language 
 */
public class LicenseDAO
{
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
    public LicenseDAO(String url,String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    
    /**
     * Return a list of licenses for the specified user task
     * @param id the id of the user task
     * @return a list of the licenses for the specified user task
     */
    public ArrayList<License> getLicenses(Long id)
    {
        ArrayList<License> licenses = new ArrayList<License>();

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("select license from taskcreateudataset_licenses where task_id=?"))
        {
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                String strLicense = rs.getString(1);
                License license = new License(strLicense);
                licenses.add(license);
            } 
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }


        return licenses;
    }
}