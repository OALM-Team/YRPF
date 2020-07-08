package fr.yuki.yrpf;

import eu.bebendorf.ajorm.AJORM;
import eu.bebendorf.ajorm.AJORMConfig;
import eu.bebendorf.ajorm.wrapper.MySQL;
import eu.bebendorf.ajorm.wrapper.SQL;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.*;
import fr.yuki.yrpf.utils.ServerConfig;
import net.onfirenetwork.onsetjava.Onset;

public class Database {

    private static SQL sql;

    public static SQL getRaw() {
        return sql;
    }

    /**
     * Initialize the connection with the database
     */
    public static void init() {
        try {
            ServerConfig serverConfig = WorldManager.getServerConfig();
            sql = new MySQL(serverConfig.getSqlHost(), 3306, serverConfig.getSqlDb(), serverConfig.getSqlUsername(), serverConfig.getSqlPassword());
            sql.setDebugMode(true);
            AJORMConfig config = new AJORMConfig()
                    .setDefaultSize(255);
            // @Yuki you can optionally add .migrate() behind the register calls to enable migration
            AJORM.register(Account.class, sql, config);
            AJORM.register(ATM.class, sql, config);
            AJORM.register(AccountJobWhitelist.class, sql, config);
            AJORM.register(Company.class, sql, config);
            AJORM.register(VehicleSeller.class, sql, config);
            AJORM.register(Garage.class, sql, config);
            AJORM.register(House.class, sql, config);
            AJORM.register(HouseItemObject.class, sql, config);
            AJORM.register(FuelPoint.class, sql, config);
            AJORM.register(Seller.class, sql, config);
            AJORM.register(PhoneContact.class, sql, config);
            AJORM.register(VehicleGarage.class, sql, config);
            AJORM.register(Inventory.class, sql, config);
            AJORM.register(JobNPC.class, sql, config);
            AJORM.register(JobLevel.class, sql, config);
            AJORM.register(ItemTemplate.class, sql, config);
            AJORM.register(OutfitPoint.class, sql, config);
            AJORM.register(GrowboxModel.class, sql, config);
            AJORM.register(JobTool.class, sql, config);
            AJORM.register(JobVehicleRental.class, sql, config);
            AJORM.register(JobOutfit.class, sql, config);
            Onset.print("Connected to the database with success");
        } catch (Exception e) {
            Onset.print("Error with database connection: " + e.toString());
        }
    }
}
