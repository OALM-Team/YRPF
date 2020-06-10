package fr.yuki.YukiRPFramework.manager;

import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

public class TimeManager {
    private static int currentHour;

    public static void init() {
        currentHour = WorldManager.getServerConfig().getStartHour();
        Onset.timer(getRealTimePerHour(), () -> tickHour());
        Onset.print("Time initialized");
    }

    public static int getRealTimePerHour() {
        return WorldManager.getServerConfig().getTimePerHour();
    }

    public static void setCurrentHourForPlayer(Player player) {
        player.callRemoteEvent("Time:SetHour", getCurrentHour());
    }

    public static void tickHour() {
        currentHour += 1;
        if(currentHour > 24) currentHour = 0;
        Onset.print("Time changed, current hour: " + currentHour);
        for(Player player : Onset.getPlayers()) {
            try {
                setCurrentHourForPlayer(player);
            }
            catch(Exception ex) {}
        }
    }

    public static int getCurrentHour() {
        return currentHour;
    }

    public static void setCurrentHour(int value) {
        currentHour = value;
    }
}
