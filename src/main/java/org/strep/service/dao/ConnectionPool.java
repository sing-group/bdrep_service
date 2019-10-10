/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.strep.service.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;
import org.strep.service.Main;

/**
 * Class to implement the static configuration of the connection pool
 *
 * @author María Novo
 * @author José Ramón Méndez
 */
public class ConnectionPool {

   static BasicDataSource ds = new BasicDataSource();

    static {
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

        try {
            ds.setUrl(Main.getProperty("url"));
            ds.setUsername(Main.getProperty("user"));
            ds.setPassword(Main.getProperty("password"));
            ds.setMaxIdle(1);
        } catch (Exception ex) {
            Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static BasicDataSource getDataSource() {
        return ds;
    }

    public static Connection getDataSourceConnection() throws SQLException {
        return ds.getConnection();
    }
}
