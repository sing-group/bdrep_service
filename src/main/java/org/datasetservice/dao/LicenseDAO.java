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

    private final String URL = "jdbc:mysql://localhost:3306/onlinepreprocessor";

    private final String USER = "springuser";

    private final String PASSWORD = "springpassword";

    public LicenseDAO()
    {

    }

    //TODO: Implement this method
    public ArrayList<License> getLicenses(Long id)
    {
        ArrayList<License> licenses = new ArrayList<License>();

        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
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