package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.job.Job;
import fr.yuki.yrpf.job.JobSpawn;
import fr.yuki.yrpf.manager.JobManager;
import fr.yuki.yrpf.manager.WorldManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

import java.util.Map;

public class ShowGatherItemListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 5) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        for(Map.Entry<String, Job> job : JobManager.getJobs().entrySet()) {
            player.sendMessage("=========> " + job.getKey());
            int i2 = 0;
            for(JobSpawn jobSpawn :  job.getValue().getJobConfig().getResources()) {
                player.sendMessage(jobSpawn.getName() + " (" + i2 + ")");
            }
        }
        return true;
    }

}
