package fr.yuki.YukiRPFramework.manager;

import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;

import java.util.HashMap;

public class SoundManager {
    private static HashMap<String, WorldObject> ambiantSounds = new HashMap<String, WorldObject>();

    public static void init() {
        createAmbiantSound("hammerhead_station", "sounds/ambiant_1.mp3",
                new Vector(127357, 78077, 1568), 6000, 0.3);
    }

    /**
     * Play a sound for players around the position
     * @param fileName The sound file name
     * @param position The position source
     * @param radius The radius max of the sound
     */
    public static void playSound3D(String fileName, Vector position, double radius, double volume) {
        for(Player player : Onset.getPlayers()) {
            if(player.getLocation().distance(position) < radius + 500) {
                player.callRemoteEvent("Sound:PlaySound3D", fileName, position.getX(), position.getY(), position.getZ(), radius, volume);
            }
        }
    }

    /**
     * Create a ambiant sound, for making like a radio for example
     * @param name The key
     * @param soundName The sound path
     * @param position The position
     * @param radius The radius
     * @param volume The volume
     */
    public static WorldObject createAmbiantSound(String name, String soundName, Vector position, int radius, double volume) {
        WorldObject worldObject = Onset.getServer().createObject(position, 1);
        worldObject.setProperty("ambiantSoundObject", 1, true);
        worldObject.setProperty("ambiantSoundName", soundName, true);
        worldObject.setProperty("ambiantSoundRadius", radius, true);
        worldObject.setProperty("ambiantSoundVolume", volume, true);
        worldObject.setStreamDistance(radius);
        ambiantSounds.put(name, worldObject);
        return worldObject;
    }

    public static HashMap<String, WorldObject> getAmbiantSounds() {
        return ambiantSounds;
    }
}
