package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.ATM;
import fr.yuki.YukiRPFramework.model.Garage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GarageDAO {
    public static ArrayList<Garage> loadGarages() throws SQLException {
        ArrayList<Garage> garages = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_garage");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Garage garage = new Garage();
            garage.setId(resultSet.getInt("id_garage"));
            garage.setName(resultSet.getString("name"));
            garage.setCostToUse(resultSet.getInt("cost_to_use"));
            garage.setX(resultSet.getDouble("x"));
            garage.setY(resultSet.getDouble("y"));
            garage.setZ(resultSet.getDouble("z"));
            garages.add(garage);
        }
        return garages;
    }
}
