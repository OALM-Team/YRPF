package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.ATM;
import fr.yuki.YukiRPFramework.model.AccountJobWhitelist;
import fr.yuki.YukiRPFramework.model.House;
import fr.yuki.YukiRPFramework.model.VehicleGarage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                .prepareStatement("UPDATE tbl_house SET id_account=?, price=?, name=? WHERE id_house=?");
        preparedStatement.setInt(1, house.getAccountId());
        preparedStatement.setInt(2, house.getPrice());
        preparedStatement.setString(3, house.getName());
        preparedStatement.setInt(4, house.getId());
        preparedStatement.execute();
    }

    public static void insertHouse(House house) throws SQLException {
        // Execute the query
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("INSERT INTO tbl_house " +
                        "(id_account, price, name, sx,sy,sz,ex,ey,ez) VALUES " +
                        "(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setInt(1, house.getAccountId());
        preparedStatement.setInt(2, house.getPrice());
        preparedStatement.setString(3, house.getName());
        preparedStatement.setDouble(4, house.getSx());
        preparedStatement.setDouble(5, house.getSy());
        preparedStatement.setDouble(6, house.getSz());
        preparedStatement.setDouble(7, house.getEx());
        preparedStatement.setDouble(8, house.getEy());
        preparedStatement.setDouble(9, house.getEz());
        preparedStatement.executeUpdate();

        ResultSet returnId = preparedStatement.getGeneratedKeys();
        if(returnId.next()) {
            house.setId(returnId.getInt(1));
        }
    }
}
