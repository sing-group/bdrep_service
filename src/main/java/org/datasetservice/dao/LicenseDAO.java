package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.datasetservice.domain.License;

public class LicenseDAO
{

    private String url;

    private String user;

    private String password;

    public LicenseDAO(String url,String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    //TODO: Implement this method
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