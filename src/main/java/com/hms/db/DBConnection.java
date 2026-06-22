package com.hms.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static Connection conn;

    public static Connection getConn() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/hospitals",
                    "root",
                    "Acchuracchu1997"
            );

            System.out.println("DB Connected");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}