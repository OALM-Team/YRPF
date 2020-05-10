package fr.yuki.YukiRPFramework;

import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.utils.ServerConfig;
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
            ServerConfig serverConfig = WorldManager.getServerConfig();
            connection = DriverManager.getConnection("jdbc:mysql://" + serverConfig.getSqlHost() + "/" + serverConfig.getSqlDb(),
                    serverConfig.getSqlUsername(), serverConfig.getSqlPassword());
            Onset.print("Connected to the database with success");
        } catch (Exception e) {
            Onset.print("Error with database connection: " + e.toString());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
