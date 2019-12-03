package org.strep.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.strep.service.domain.Language;

/**
 * Data access object for Language
 */
public class LanguageDAO {

    /**
     * The connection url to the database
     */
    private static final Logger logger = LogManager.getLogger(LanguageDAO.class);

    /**
     * A constructor for create instances of DatasetDAO
     */
    public LanguageDAO() {
    }

    /**
     * Return a list of languages associated to the specified user task
     *
     * @param id the id of the task
     * @return a list of languages associated to specified user task
     */
    public ArrayList<Language> getLanguages(Long id) {
        ArrayList<Language> languages = new ArrayList<>();

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM taskcreateudataset_languages WHERE task_id = ?")) {
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String strLanguage = rs.getString(2);
                Language language = new Language(strLanguage);
                languages.add(language);
            }
        } catch (SQLException sqlException) {
            logger.warn("[ERROR getLanguages]: " + sqlException.getMessage());
        }
        return languages;
    }
}
