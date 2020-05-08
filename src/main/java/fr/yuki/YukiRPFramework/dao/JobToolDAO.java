package fr.yuki.YukiRPFramework.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.JobNPC;
import fr.yuki.YukiRPFramework.model.JobNPCListItem;
import fr.yuki.YukiRPFramework.model.JobTool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JobToolDAO {
    public static ArrayList<JobTool> loadJobTools() throws SQLException {
        ArrayList<JobTool> jobTools = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_job_tool");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            JobTool jobTool = new JobTool();

            jobTool.setId(resultSet.getInt("id_job_tool"));
            jobTool.setModelId(resultSet.getInt("model_id"));
            jobTool.setName(resultSet.getString("name"));
            jobTool.setJobType(resultSet.getString("job_type"));
            jobTool.setLevelRequired(resultSet.getInt("level_required"));
            jobTool.setJobToolType(resultSet.getString("job_tool_type"));
            jobTool.setReward(resultSet.getInt("reward"));
            jobTool.setX(resultSet.getDouble("x"));
            jobTool.setY(resultSet.getDouble("y"));
            jobTool.setZ(resultSet.getDouble("z"));
            jobTool.setRx(resultSet.getDouble("r_x"));
            jobTool.setRy(resultSet.getDouble("r_y"));
            jobTool.setRz(resultSet.getDouble("r_z"));
            jobTool.setSx(resultSet.getDouble("s_x"));
            jobTool.setSy(resultSet.getDouble("s_y"));
            jobTool.setSz(resultSet.getDouble("s_z"));

            jobTools.add(jobTool);
        }
        return jobTools;
    }
}
