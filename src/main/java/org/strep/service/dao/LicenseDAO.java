package org.strep.service.dao;

import java.sql.Connection;
import java.sql.DriverManager;
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
                PreparedStatement preparedStatement = connection.prepareStatement("select license from taskcreateudataset_licenses where task_id=?")) {
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
