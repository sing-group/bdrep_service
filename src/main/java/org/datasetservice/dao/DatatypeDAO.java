package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.datasetservice.domain.Datatype;

public class DatatypeDAO
{

    private String url;
    
    private String user;
    
    private String password;
    
    public DatatypeDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;

    }

    //TODO: Implement this method
    public ArrayList<Datatype> getDatatypes(Long id)
    {
        ArrayList<Datatype> datatypes = new ArrayList<Datatype>();

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("select * from taskcreateudataset_datatypes where task_id=?"))
        {
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                String strDatatype = rs.getString(2);
                Datatype datatype = new Datatype(strDatatype);
                datatypes.add(datatype);
            }

        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

        return datatypes;
    }
}