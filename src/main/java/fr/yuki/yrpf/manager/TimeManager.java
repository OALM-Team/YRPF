package fr.yuki.yrpf.manager;

import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

public class TimeManager {
    private static float currentHour;

    public static void init() {
        currentHour = WorldManager.getServerConfig().getStartHour();
        Onset.timer(getRealTimePerHour() / 60, () -> tickHour());
        Onset.print("Time initialized");
    }

    public static int getRealTimePerHour() {
        return WorldManager.getServerConfig().getTimePerHour();
    }

    public static void setCurrentHourForPlayer(Player player) {
        player.callRemoteEvent("Time:SetHour", String.valueOf(getCurrentHour()));
    }

    public static void tickHour() {
        currentHour += 1f / 60f;
        if(currentHour > 18 || currentHour < 8) currentHour += (1f / 60f) * 4;
        if(currentHour > 24) currentHour = 0;
        //Onset.print("Time changed, current hour: " + currentHour);
        for(Player player : Onset.getPlayers()) {
            try {
                setCurrentHourForPlayer(player);
            }
            catch(Exception ex) {}
        }
    }

    public static float getCurrentHour() {
        return currentHour;
    }

    public static void setCurrentHour(float value) {
        currentHour = value;
        for(Player player : Onset.getPlayers()) {
            try {
                setCurrentHourForPlayer(player);
            }
            catch(Exception ex) {}
        }
    }
}
