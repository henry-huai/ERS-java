package dev.huai.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionService {
    public Connection establishConnection() {

        try {
            //registering our JDBC driver in the classpath
            //
            Class.forName("org.postgresql.Driver");
            String url = System.getenv("Project1DB_URL");
            String username = System.getenv("Project1DB_Username");
            String password = System.getenv("Project1DB_Password");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

}
