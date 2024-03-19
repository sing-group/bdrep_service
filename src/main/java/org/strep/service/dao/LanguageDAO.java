/*-
 * #%L
 * STRep Service
 * %%
 * Copyright (C) 2019 - 2024 SING Group (University of Vigo)
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
