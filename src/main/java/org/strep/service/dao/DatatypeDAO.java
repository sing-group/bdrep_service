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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.strep.service.domain.Datatype;

/**
 * Datatype data access object
 */
public class DatatypeDAO {

    /**
     * For logging purposes
     */
    private static final Logger logger = LogManager.getLogger(DatatypeDAO.class);

    /**
     * A constructor for create instances of DatatypeDAO
     */
    public DatatypeDAO() {}

    /**
     * Return a list of the datatypes associated a user task
     *
     * @param id the id of the user task
     * @return a list of the datatypes associated a user task
     */
    public ArrayList<Datatype> getDatatypes(Long id) {
        ArrayList<Datatype> datatypes = new ArrayList<>();

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM taskcreateudataset_datatypes WHERE task_id=?")) {
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String strDatatype = rs.getString(2);
                Datatype datatype = new Datatype(strDatatype);
                datatypes.add(datatype);
            }
        } catch (SQLException sqlException) {
            logger.warn("[ERROR getDatatypes]: " + sqlException.getMessage());
        }

        return datatypes;
    }
}
