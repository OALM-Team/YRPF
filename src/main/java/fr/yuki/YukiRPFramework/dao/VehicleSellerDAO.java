package fr.yuki.YukiRPFramework.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.model.SellListItem;
import fr.yuki.YukiRPFramework.model.VehicleGarage;
import fr.yuki.YukiRPFramework.model.VehicleSeller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleSellerDAO {

    public static ArrayList<VehicleSeller> loadVehicleSellers() throws SQLException {
        ArrayList<VehicleSeller> vehicleSellers = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_vehicle_seller");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            VehicleSeller vehicleSeller = new VehicleSeller();

            vehicleSeller.setId(resultSet.getInt("id_vehicle_seller"));
            vehicleSeller.setName(resultSet.getString("name"));
            vehicleSeller.setNpcClothing(resultSet.getInt("npc_clothing"));
            vehicleSeller.setX(resultSet.getDouble("x"));
            vehicleSeller.setY(resultSet.getDouble("y"));
            vehicleSeller.setZ(resultSet.getDouble("z"));
            vehicleSeller.setH(resultSet.getDouble("h"));
            vehicleSeller.setsX(resultSet.getDouble("s_x"));
            vehicleSeller.setsY(resultSet.getDouble("s_y"));
            vehicleSeller.setsZ(resultSet.getDouble("s_z"));
            vehicleSeller.setsH(resultSet.getDouble("s_h"));
            vehicleSeller.setSellList(new Gson().fromJson(resultSet.getString("sell_list"),
                    new TypeToken<ArrayList<SellListItem>>(){}.getType()));

            vehicleSellers.add(vehicleSeller);
        }
        preparedStatement.close();
        resultSet.close();
        return vehicleSellers;
    }
}
