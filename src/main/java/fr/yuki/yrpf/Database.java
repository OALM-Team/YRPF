package fr.yuki.yrpf;

import eu.bebendorf.ajorm.AJORM;
import eu.bebendorf.ajorm.AJORMConfig;
import eu.bebendorf.ajorm.wrapper.MySQL;
import eu.bebendorf.ajorm.wrapper.SQL;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.*;
import fr.yuki.yrpf.utils.ServerConfig;
import net.onfirenetwork.onsetjava.Onset;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static Connection connection;
    private static SQL sql;

    /**
     * Initialize the connection with the database
     */
    public static void init() {
        try {
            ServerConfig serverConfig = WorldManager.getServerConfig();
            connection = DriverManager.getConnection("jdbc:mysql://" + serverConfig.getSqlHost() + "/" + serverConfig.getSqlDb() + "?autoReconnect=true",
                    serverConfig.getSqlUsername(), serverConfig.getSqlPassword());
            sql = new MySQL(serverConfig.getSqlHost(), 3306, serverConfig.getSqlDb(), serverConfig.getSqlUsername(), serverConfig.getSqlPassword());
            AJORMConfig config = new AJORMConfig()
                    .setDefaultSize(255);
            AJORM.register(Account.class, sql, config);
            AJORM.register(ATM.class, sql, config);
            AJORM.register(AccountJobWhitelist.class, sql, config);
            AJORM.register(Company.class, sql, config);
            AJORM.register(VehicleSeller.class, sql, config);
            AJORM.register(Garage.class, sql, config);
            AJORM.register(House.class, sql, config);
            AJORM.register(FuelPoint.class, sql, config);
            Onset.print("Connected to the database with success");
        } catch (Exception e) {
            Onset.print("Error with database connection: " + e.toString());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
