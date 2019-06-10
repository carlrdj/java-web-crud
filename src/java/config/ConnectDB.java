/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author carlr
 */
public class ConnectDB {
    public Connection getConnection() throws SQLException {
        Connection cn = null;

        try {
            Class.forName(driver).newInstance();
            cn = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException 
                | InstantiationException 
                | IllegalAccessException e) {

            throw new SQLException(e.getMessage());
        }

        return cn;
    }

    private final String url = "jdbc:mysql://160.153.57.64:3306/db_app";
    private final String driver = "com.mysql.jdbc.Driver";
    private final String user = "db_app";
    private final String password = "$db_app$";
}
