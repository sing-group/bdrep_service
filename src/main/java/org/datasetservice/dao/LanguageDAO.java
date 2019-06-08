package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.datasetservice.domain.Language;

public class LanguageDAO
{

    private final String URL = "jdbc:mysql://localhost:3306/onlinepreprocessor";

    private final String USER = "springuser";

    private final String PASSWORD = "springpassword";

    public LanguageDAO()
    {

    }

    public ArrayList<Language> getLanguages(Long id)
    {
        ArrayList<Language> languages = new ArrayList<Language>();

        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
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