package fr.yuki.YukiRPFramework.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.JobNPC;
import fr.yuki.YukiRPFramework.model.JobNPCListItem;
import fr.yuki.YukiRPFramework.model.SellListItem;
import fr.yuki.YukiRPFramework.model.VehicleSeller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JobNPCDAO {
    public static ArrayList<JobNPC> loadJobNPCS() throws SQLException {
        ArrayList<JobNPC> jobNPCS = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_job_npc");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            JobNPC jobNPC = new JobNPC();

            jobNPC.setId(resultSet.getInt("id_job_npc"));
            jobNPC.setJobId(resultSet.getString("id_job"));
            jobNPC.setX(resultSet.getDouble("x"));
            jobNPC.setY(resultSet.getDouble("y"));
            jobNPC.setZ(resultSet.getDouble("z"));
            jobNPC.setH(resultSet.getDouble("h"));
            jobNPC.setNpcClothing(resultSet.getInt("npc_clothing"));
            jobNPC.setBuyList(new Gson().fromJson(resultSet.getString("buy_list"),
                    new TypeToken<ArrayList<JobNPCListItem>>(){}.getType()));
            jobNPC.setSellList(new Gson().fromJson(resultSet.getString("sell_list"),
                    new TypeToken<ArrayList<JobNPCListItem>>(){}.getType()));

            jobNPCS.add(jobNPC);
        }
        return jobNPCS;
    }
}
