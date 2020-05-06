package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.job.Job;
import fr.yuki.YukiRPFramework.job.JobSpawn;
import fr.yuki.YukiRPFramework.job.JobSpawnPosition;
import fr.yuki.YukiRPFramework.job.harvest.HarvestableObject;
import fr.yuki.YukiRPFramework.manager.JobManager;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

import java.util.ArrayList;
import java.util.Map;

public class AddGatherItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        Job job = new ArrayList<Job>(JobManager.getJobs().values()).get(Integer.parseInt(args[0]));
        JobSpawn jobSpawn = job.getJobConfig().getResources().get(Integer.parseInt(args[1]));
        JobSpawnPosition jobSpawnPosition = new JobSpawnPosition();
        Vector playerLocation = player.getLocation();
        jobSpawnPosition.setX(playerLocation.getX());
        jobSpawnPosition.setY(playerLocation.getY());
        jobSpawnPosition.setZ(playerLocation.getZ());
        jobSpawn.getSpawns().add(jobSpawnPosition);
        job.saveConfig();
        player.sendMessage("Added " + jobSpawn.getName() + " at this location");
        return true;
    }
}
