package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.JobLevel;
import fr.yuki.YukiRPFramework.model.JobVehicleRental;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JobVehicleRentalDAO {
    public static ArrayList<JobVehicleRental> loadJobVehicleRental() throws SQLException {
        ArrayList<JobVehicleRental> jobVehicleRentals = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_job_vehicle_rental");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            JobVehicleRental jobVehicleRental = new JobVehicleRental();

            jobVehicleRental.setId(resultSet.getInt("id_job_vehicle_rental"));
            jobVehicleRental.setJobId(resultSet.getString("id_job"));
            jobVehicleRental.setLevelRequired(resultSet.getInt("level_required"));
            jobVehicleRental.setName(resultSet.getString("name"));
            jobVehicleRental.setVehicleModelId(resultSet.getInt("vehicle_model_id"));
            jobVehicleRental.setCost(resultSet.getInt("cost"));
            jobVehicleRental.setX(resultSet.getDouble("x"));
            jobVehicleRental.setY(resultSet.getDouble("y"));
            jobVehicleRental.setZ(resultSet.getDouble("z"));
            jobVehicleRental.setSpawnX(resultSet.getDouble("spawn_x"));
            jobVehicleRental.setSpawnY(resultSet.getDouble("spawn_y"));
            jobVehicleRental.setSpawnZ(resultSet.getDouble("spawn_z"));
            jobVehicleRentals.add(jobVehicleRental);
        }
        return jobVehicleRentals;
    }
}
