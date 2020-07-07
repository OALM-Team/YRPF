package fr.yuki.yrpf.dao;

import fr.yuki.yrpf.Database;
import fr.yuki.yrpf.model.JobLevel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JobLevelDAO {
    public static ArrayList<JobLevel> loadJobLevels() throws SQLException {
        ArrayList<JobLevel> jobLevels = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_job_level");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            JobLevel jobLevel = new JobLevel();

            jobLevel.setId(resultSet.getInt("id_job_level"));
            jobLevel.setJobId(resultSet.getString("id_job"));
            jobLevel.setName(resultSet.getString("name"));
            jobLevel.setLevel(resultSet.getInt("level"));
            jobLevel.setExpFloor(resultSet.getInt("exp_floor"));

            jobLevels.add(jobLevel);
        }
        return jobLevels;
    }
}
