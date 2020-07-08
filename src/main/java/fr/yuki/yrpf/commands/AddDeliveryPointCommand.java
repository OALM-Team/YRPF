package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.job.JobSpawnPosition;
import fr.yuki.yrpf.manager.JobManager;
import fr.yuki.yrpf.manager.WorldManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class AddDeliveryPointCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        JobSpawnPosition jobSpawnPosition = new JobSpawnPosition();
        jobSpawnPosition.setX(player.getLocation().getX());
        jobSpawnPosition.setY(player.getLocation().getY());
        jobSpawnPosition.setZ(player.getLocation().getZ());
        JobManager.getDeliveryPointConfig().getPoints().get(args[0]).add(jobSpawnPosition);
        JobManager.saveDeliveryPoints();
        player.sendMessage("Add new delivery location at this location");
        return true;
    }
}
