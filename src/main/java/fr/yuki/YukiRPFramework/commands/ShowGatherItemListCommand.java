package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.job.Job;
import fr.yuki.YukiRPFramework.job.JobSpawn;
import fr.yuki.YukiRPFramework.job.harvest.HarvestableObject;
import fr.yuki.YukiRPFramework.manager.JobManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

import java.util.Map;

public class ShowGatherItemListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        int i1 = 0;
        for(Map.Entry<String, Job> job : JobManager.getJobs().entrySet()) {
            player.sendMessage("=========> " + job.getKey() + " (" + i1 + ")");
            int i2 = 0;
            for(JobSpawn jobSpawn :  job.getValue().getJobConfig().getResources()) {
                player.sendMessage(jobSpawn.getName() + " (" + i2 + ")");
                i2++;
            }
            i1++;
        }
        return true;
    }
}
