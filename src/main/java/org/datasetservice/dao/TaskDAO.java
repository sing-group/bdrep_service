package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.datasetservice.domain.Task;

public class TaskDAO {

    private String url;

    private String user;

    private String password;

    public TaskDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Task getTask(Long id)
    {
        Task task = null;

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM task where id=?")
        )
        {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next())
            {
                task = new Task(rs.getLong(1), null, rs.getString(3), rs.getString(2));
            }

            
        }
        catch(SQLException sqlException)
        {
            System.out.println(sqlException.toString());
        }
        
        return task;
    }

    public void changeState(String message, String state, Long id)
    {
        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE task SET message = ?, state = ? where id = ?"))
        {
            preparedStatement.setString(1, message);
            preparedStatement.setString(2, state);
            preparedStatement.setLong(3, id);

            preparedStatement.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            System.out.println(sqlException.toString());
        }
    } 
    
}