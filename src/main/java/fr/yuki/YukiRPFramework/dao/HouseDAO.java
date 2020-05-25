package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.ATM;
import fr.yuki.YukiRPFramework.model.House;
import fr.yuki.YukiRPFramework.model.VehicleGarage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HouseDAO {
    public static ArrayList<House> loadHouses() throws SQLException {
        ArrayList<House> houses = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_house");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            House house = new House();
            house.setId(resultSet.getInt("id_house"));
            house.setAccountId(resultSet.getInt("id_account"));
            house.setPrice(resultSet.getInt("price"));
            house.setName(resultSet.getString("name"));
            house.setSx(resultSet.getDouble("sx"));
            house.setSy(resultSet.getDouble("sy"));
            house.setSz(resultSet.getDouble("sz"));
            house.setEx(resultSet.getDouble("ex"));
            house.setEy(resultSet.getDouble("ey"));
            house.setEz(resultSet.getDouble("ez"));
            houses.add(house);
        }
        return houses;
    }

    public static void saveHouse(House house) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("UPDATE tbl_house SET id_account=? WHERE id_house=?");
        preparedStatement.setInt(1, house.getAccountId());
        preparedStatement.setInt(2, house.getId());
        preparedStatement.execute();
    }
}
