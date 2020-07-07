package fr.yuki.yrpf.dao;

import fr.yuki.yrpf.Database;
import fr.yuki.yrpf.model.HouseItemObject;

import java.sql.*;
import java.util.ArrayList;

public class HouseItemDAO {
    public static ArrayList<HouseItemObject> loadHouseItems() throws SQLException {
        ArrayList<HouseItemObject> houseItemObjects = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_house_item");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            HouseItemObject houseItemObject = new HouseItemObject();
            houseItemObject.setId(resultSet.getInt("id_house_item"));
            houseItemObject.setModelId(resultSet.getInt("model_id"));
            houseItemObject.setFunctionId(resultSet.getInt("function_id"));
            houseItemObject.setX(resultSet.getDouble("x"));
            houseItemObject.setY(resultSet.getDouble("y"));
            houseItemObject.setZ(resultSet.getDouble("z"));
            houseItemObject.setRx(resultSet.getDouble("rx"));
            houseItemObject.setRy(resultSet.getDouble("ry"));
            houseItemObject.setRz(resultSet.getDouble("rz"));
            houseItemObjects.add(houseItemObject);
        }
        return houseItemObjects;
    }

    public static void insertHouseItem(HouseItemObject houseItemObject) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("INSERT INTO tbl_house_item " +
                        "(model_id, function_id, id_house, x, y, z, rx, ry, rz) VALUES " +
                        "(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, houseItemObject.getModelId());
        preparedStatement.setInt(2, houseItemObject.getFunctionId());
        preparedStatement.setInt(3, houseItemObject.getHouse().getId());
        preparedStatement.setDouble(4, houseItemObject.getX());
        preparedStatement.setDouble(5, houseItemObject.getY());
        preparedStatement.setDouble(6, houseItemObject.getZ());
        preparedStatement.setDouble(7, houseItemObject.getRx());
        preparedStatement.setDouble(8, houseItemObject.getRy());
        preparedStatement.setDouble(9, houseItemObject.getRz());
        preparedStatement.executeUpdate();

        ResultSet returnId = preparedStatement.getGeneratedKeys();
        if(returnId.next()) {
            houseItemObject.setId(returnId.getInt(1));
        }
    }

    public static void deleteHouseItem(HouseItemObject houseItemObject) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("DELETE FROM tbl_house_item WHERE id_house_item=?");
        preparedStatement.setInt(1, houseItemObject.getId());
        preparedStatement.executeUpdate();
    }
}
