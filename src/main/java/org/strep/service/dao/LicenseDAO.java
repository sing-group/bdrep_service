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

import org.strep.service.domain.License;

/**
 * Data access object for Language
 */
public class LicenseDAO {

    /**
     * The connection url to the database
     */
    private static final Logger logger = LogManager.getLogger(LicenseDAO.class);

    /**
     * A constructor for create instances of DatasetDAO
     */
    public LicenseDAO() {
    }

    /**
     * Return a list of licenses for the specified user task
     *
     * @param id the id of the user task
     * @return a list of the licenses for the specified user task
     */
    public ArrayList<License> getLicenses(Long id) {
        ArrayList<License> licenses = new ArrayList<>();

        try (Connection connection = ConnectionPool.getDataSourceConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT license FROM taskcreateudataset_licenses WHERE task_id=?")) {
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String strLicense = rs.getString(1);
                License license = new License(strLicense);
                licenses.add(license);
            }
        } catch (SQLException sqlException) {
            logger.warn("[ERROR getLicenses]: " + sqlException.getMessage());
        }

        return licenses;
    }
}
