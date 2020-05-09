package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.job.JobSpawnPosition;
import fr.yuki.YukiRPFramework.manager.JobManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class AddDeliveryPointCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
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
