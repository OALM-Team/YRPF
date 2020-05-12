package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.AccountJobWhitelist;
import fr.yuki.YukiRPFramework.model.JobOutfit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JobOutfitDAO {
    public static ArrayList<JobOutfit> loadJobOutfits() throws SQLException {
        ArrayList<JobOutfit> jobOutfits = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_job_outfit");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            JobOutfit jobOutfit = new JobOutfit();
            jobOutfit.setId(resultSet.getInt("id_job_outfit"));
            jobOutfit.setJobId(resultSet.getString("id_job"));
            jobOutfit.setLevelRequired(resultSet.getInt("level_required"));
            jobOutfit.setName(resultSet.getString("name"));
            jobOutfit.setOutfit(resultSet.getString("outfit"));
            jobOutfit.setX(resultSet.getDouble("x"));
            jobOutfit.setY(resultSet.getDouble("y"));
            jobOutfit.setZ(resultSet.getDouble("z"));

            jobOutfits.add(jobOutfit);
        }
        return jobOutfits;
    }
}
