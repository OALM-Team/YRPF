package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.ATM;
import fr.yuki.YukiRPFramework.model.VehicleGarage;

import java.sql.*;
import java.util.ArrayList;

public class VehicleGarageDAO {
    public static VehicleGarage createVehicleGarage(VehicleGarage vehicleGarage) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("INSERT INTO tbl_vehicle_garage " +
                        "(id_garage, id_last_garage, id_model, damage, licence_plate, color, created_at, updated_at, uuid, owner) VALUES " +
                        "(?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, vehicleGarage.getGarageId());
        preparedStatement.setInt(2, vehicleGarage.getGarageLastId());
        preparedStatement.setInt(3, vehicleGarage.getModelId());
        preparedStatement.setDouble(4, vehicleGarage.getDamage());
        preparedStatement.setString(5, vehicleGarage.getLicencePlate());
        preparedStatement.setString(6, vehicleGarage.getColor());
        preparedStatement.setDate(7, new Date(new java.util.Date().getTime()));
        preparedStatement.setDate(8, new Date(new java.util.Date().getTime()));
        preparedStatement.setString(9, vehicleGarage.getUuid());
        preparedStatement.setInt(10, vehicleGarage.getOwner());
        preparedStatement.executeUpdate();

        ResultSet returnId = preparedStatement.getGeneratedKeys();
        if(returnId.next()) {
            vehicleGarage.setVehicleGarageId(returnId.getInt(1));
        } else {
            return null;
        }
        return vehicleGarage;
    }

    public static ArrayList<VehicleGarage> loadVehiclesGarage() throws SQLException {
        ArrayList<VehicleGarage> vehicleGarages = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_vehicle_garage");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            VehicleGarage vehicleGarage = new VehicleGarage();

            vehicleGarage.setOwner(resultSet.getInt("owner"));
            vehicleGarage.setUuid(resultSet.getString("uuid"));
            vehicleGarage.setGarageId(resultSet.getInt("id_garage"));
            vehicleGarage.setGarageLastId(resultSet.getInt("id_last_garage"));
            vehicleGarage.setModelId(resultSet.getInt("id_model"));
            vehicleGarage.setDamage(0);
            vehicleGarage.setLicencePlate(resultSet.getString("licence_plate"));
            vehicleGarage.setColor(resultSet.getString("color"));
            vehicleGarage.setCreatedAt(resultSet.getDate("created_at"));
            vehicleGarage.setUpdateAt(resultSet.getDate("updated_at"));

            vehicleGarages.add(vehicleGarage);
        }
        return vehicleGarages;
    }

    public static void saveVehicleGarage(VehicleGarage vehicleGarage) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("UPDATE tbl_vehicle_garage SET id_garage=?, id_last_garage=?, color=?, licence_plate=? WHERE id_vehicle_garage=?");
        preparedStatement.setInt(1, vehicleGarage.getGarageId());
        preparedStatement.setInt(2, vehicleGarage.getGarageLastId());
        preparedStatement.setString(3, vehicleGarage.getColor());
        preparedStatement.setString(4, vehicleGarage.getLicencePlate());
        preparedStatement.setInt(5, vehicleGarage.getVehicleGarageId());
        preparedStatement.execute();
    }
}
