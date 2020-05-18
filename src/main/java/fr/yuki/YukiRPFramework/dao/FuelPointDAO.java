package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.FuelPoint;
import fr.yuki.YukiRPFramework.model.JobOutfit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FuelPointDAO {
    public static ArrayList<FuelPoint> loadFuelPoints() throws SQLException {
        ArrayList<FuelPoint> fuelPoints = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_fuel_point");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            FuelPoint fuelPoint = new FuelPoint();
            fuelPoint.setId(resultSet.getInt("id_fuel_point"));
            fuelPoint.setX(resultSet.getDouble("x"));
            fuelPoint.setY(resultSet.getDouble("y"));
            fuelPoint.setZ(resultSet.getDouble("z"));
            fuelPoint.setPrice(resultSet.getInt("price"));

            fuelPoints.add(fuelPoint);
        }
        return fuelPoints;
    }
}
