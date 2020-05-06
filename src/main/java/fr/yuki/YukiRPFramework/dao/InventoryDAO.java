package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.model.ItemTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class InventoryDAO {

    /**
     * Create a new inventory empty
     * @return The inventory
     */
    public static Inventory createInventory() throws SQLException {
        // Create the object
        Inventory inventory = new Inventory();
        inventory.setInventoryType(-1);
        inventory.setInventoryItemType(-1);
        inventory.setCharacterId(-1);
        inventory.setVehicleId(-1);
        inventory.setContent("[]");

        // Insert the inventory
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("INSERT INTO tbl_inventory " +
                        "(inventory_type, inventory_item_type, character_id, vehicle_id, content) VALUES " +
                        "(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, inventory.getInventoryType());
        preparedStatement.setInt(2, inventory.getInventoryItemType());
        preparedStatement.setInt(3, inventory.getCharacterId());
        preparedStatement.setInt(4, inventory.getVehicleId());
        preparedStatement.setString(5, inventory.getContent());
        preparedStatement.executeUpdate();

        ResultSet returnId = preparedStatement.getGeneratedKeys();
        if(returnId.next()) {
            inventory.setId(returnId.getInt(1));
        } else {
            return null;
        }
        return inventory;
    }

    /**
     * Update the inventory in the database
     * @param inventory The inventory
     * @throws SQLException The error
     */
    public static void updateInventory(Inventory inventory) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("UPDATE tbl_inventory SET inventory_type=?, inventory_item_type=?, " +
                        "character_id=?, vehicle_id=?, content=? WHERE id_inventory=?");
        preparedStatement.setInt(1, inventory.getInventoryType());
        preparedStatement.setInt(2, inventory.getInventoryItemType());
        preparedStatement.setInt(3, inventory.getCharacterId());
        preparedStatement.setInt(4, inventory.getVehicleId());
        preparedStatement.setString(5, inventory.getContent());
        preparedStatement.setInt(6, inventory.getId());
        preparedStatement.execute();
    }

    public static HashMap<Integer, Inventory> loadInventories() throws SQLException {
        HashMap<Integer, Inventory> inventoryHashMap = new HashMap<Integer, Inventory>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_inventory");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Inventory inventory = new Inventory();
            inventory.setId(resultSet.getInt("id_inventory"));
            inventory.setInventoryType(resultSet.getInt("inventory_type"));
            inventory.setInventoryType(resultSet.getInt("inventory_item_type"));
            inventory.setCharacterId(resultSet.getInt("character_id"));
            inventory.setVehicleId(resultSet.getInt("vehicle_id"));
            inventory.setContent(resultSet.getString("content"));
            inventory.parseContent();
            inventoryHashMap.put(inventory.getId(), inventory);
        }
        return inventoryHashMap;
    }
}
