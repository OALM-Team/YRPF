package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.job.Job;
import fr.yuki.yrpf.job.JobSpawn;
import fr.yuki.yrpf.job.JobSpawnPosition;
import fr.yuki.yrpf.manager.JobManager;
import fr.yuki.yrpf.manager.WorldManager;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

import java.util.ArrayList;

public class AddGatherItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
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
