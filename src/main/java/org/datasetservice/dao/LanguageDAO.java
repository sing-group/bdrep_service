package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.datasetservice.domain.Language;

/**
 * Data access object for Language 
 */
public class LanguageDAO
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
    public LanguageDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;

    }

    /**
     * Return a list of languages associated to the specified user task
     * @param id the id of the task
     * @return a list of languages associated to specified user task
     */
    public ArrayList<Language> getLanguages(Long id)
    {
        ArrayList<Language> languages = new ArrayList<Language>();

        try(Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement("select * from taskcreateudataset_languages where task_id = ?"))
        {
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            
            while(rs.next())
            {
                String strLanguage = rs.getString(2);
                Language language = new Language(strLanguage);
                languages.add(language);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

        return languages;
    }
}