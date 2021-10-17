package dev.huai.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionService {
    public Connection establishConnection() {

        try {
            //registering our JDBC driver in the classpath
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://project1-db.cus4q5cij5bd.us-east-2.rds.amazonaws.com:5432/postgres";
            String username = "postgres";
            String password = "7499";


//            String url = System.getenv("Project1DB_URL");
//            String username = System.getenv("Project1DB_Username");
//            String password = System.getenv("Project1DB_Password");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

}
