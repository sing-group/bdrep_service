package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.datasetservice.domain.Datatype;

/**
 * Datatype data access object
 */
public class DatatypeDAO
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
     * A constructor for create instances of DatatypeDAO
     * @param url The connection url to the database
     * @param user The user of the database
     * @param password The password for the database
     */
    public DatatypeDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;

    }

    /**
     * Return a list of the datatypes associated a user task
     * @param id the id of the user task
     * @return a list of the datatypes associated a user task
     */
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