package fr.yuki.YukiRPFramework;

import net.onfirenetwork.onsetjava.Onset;

import java.sql.Connection;
import java.sql.DriverManager;
import com.mysql.cj.jdbc.Driver;

public class Database {
    private static Connection connection;

    /**
     * Initialize the connection with the database
     */
    public static void init() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/yrpf", "root", "root");
            Onset.print("Connected to the database with success");
        } catch (Exception e) {
            Onset.print("Error with database connection: " + e.toString());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
