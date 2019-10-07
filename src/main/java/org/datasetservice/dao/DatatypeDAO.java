package org.datasetservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.datasetservice.domain.Datatype;

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
                PreparedStatement preparedStatement = connection.prepareStatement("select * from taskcreateudataset_datatypes where task_id=?")) {
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String strDatatype = rs.getString(2);
                Datatype datatype = new Datatype(strDatatype);
                datatypes.add(datatype);
            }
        } catch (SQLException sqlException) {
            logger.warn("[ERROR]: " + sqlException.getMessage());
        }

        return datatypes;
    }
}
