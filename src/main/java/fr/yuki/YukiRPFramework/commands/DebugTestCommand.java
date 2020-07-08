package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.job.Job;
import fr.yuki.YukiRPFramework.job.JobSpawn;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.JobManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

import java.lang.reflect.Field;
import java.util.Map;

public class DebugTestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        for(Map.Entry<String, Job> job : JobManager.getJobs().entrySet()) {
            player.sendMessage(job.getKey() + " : (" + job.getValue().getWorldHarvestObjects().size() + ")");
        }
        player.sendMessage("States : (" + CharacterManager.getCharacterStates().size() + ")");
        player.sendMessage("DelayCount : (" + getDelayCount() + ")");
        return true;
    }

    private int getDelayCount() {
        Field f1 = null;
        try {
            f1 = Class.forName("net.onfirenetwork.onsetjava.jni.ServerJNI").getDeclaredField("instance");
            f1.setAccessible(true);
            Object server = f1.get(null);
            Field f2 = Class.forName("net.onfirenetwork.onsetjava.jni.ServerJNI").getField("packageBus");
            Object packageBus = f2.get(server);
            Field f3 = Class.forName("net.onfirenetwork.onsetjava.jni.PackageBus").getDeclaredField("delayFunctionMap");
            f3.setAccessible(true);
            Map<Integer,Runnable> map = (Map<Integer, Runnable>) f3.get(packageBus);
            return map.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
