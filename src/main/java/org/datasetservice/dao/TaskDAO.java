package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.datasetservice.domain.Task;

/**
 * Data access object for task objects
 */
public class TaskDAO {
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
     * A constructor for create instances of TaskDAO
     * @param url The connection url to the database
     * @param user The user of the database
     * @param password The password for the database
     */
    public TaskDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Return the specified task
     * @param id the id of the task
     * @return the specified task
     */
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

    /**
     * Change the state of the task
     * @param message a information message for failed tasks
     * @param state the state of the task: waiting, executing, failed, success
     * @param id the id of the task
     */
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