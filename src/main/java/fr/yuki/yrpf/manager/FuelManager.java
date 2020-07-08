package fr.yuki.yrpf.manager;

import eu.bebendorf.ajorm.Repo;
import fr.yuki.yrpf.character.CharacterLoopAnimation;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.character.CharacterToolAnimation;
import fr.yuki.yrpf.model.FuelPoint;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Pickup;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.List;

public class FuelManager {
    private static List<FuelPoint> fuelPoints;

    public static void init() {
        fuelPoints = Repo.get(FuelPoint.class).all();
        Onset.print("Loaded " + fuelPoints.size() + " fuel point(s) from the database");
        spawnFuelPoints();

        Onset.timer(30000, () -> { // 30 minutes
            for(Vehicle v : Onset.getVehicles()) {
                if(v.isEngineOn()) {
                    if(v.getPropertyInt("fuel") > 0) {
                        v.setProperty("fuel", v.getPropertyInt("fuel") - 1, true);

                        if(v.getPropertyInt("fuel") <= 0) {
                            if(v.isEngineOn()) {
                                v.setEngineOn(false);
                            }
                        }
                    } else {
                        if(v.isEngineOn()) {
                            v.setEngineOn(false);
                        }
                    }
                }
            }
        });
    }

    public static void spawnFuelPoints() {
        for(FuelPoint fuelPoint : fuelPoints) {
            Pickup pickup = Onset.getServer().createPickup(new Vector(fuelPoint.getX(), fuelPoint.getY(), fuelPoint.getZ()), 1);
            ModdingManager.assignCustomModel(pickup, 50008);
        }
    }

    public static FuelPoint getNearestFuelPoint(Player player) {
        for(FuelPoint fuelPoint : fuelPoints) {
            if(fuelPoint.isNear(player)) {
                return fuelPoint;
            }
        }
        return null;
    }

    public static boolean interactFuelPoint(Player player, boolean fake) {
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        if(!characterState.canInteract()) {
            return false;
        }
        if(player.getVehicle() != null) return false;
        if(!fake) {
            FuelPoint fuelPoint = getNearestFuelPoint(player);
            if(fuelPoint == null) return false;
        }

        Vehicle nearbyVehicle = VehicleManager.getNearestVehicle(player.getLocation());
        if(nearbyVehicle == null) return false;
        if(nearbyVehicle.getLocation().distance(player.getLocation()) > 300) return false;
        if(nearbyVehicle.getPropertyInt("fuel") >= 100) return false;
        CharacterManager.setCharacterFreeze(player, true);
        nearbyVehicle.setProperty("fuel", 0, true);

        CharacterLoopAnimation characterLoopAnimation = new CharacterLoopAnimation(player, Animation.FISHING, 10000, 1,
                "sounds/water_fill.mp3");
        characterLoopAnimation.setTool(new CharacterToolAnimation(507, new Vector(5,10,-15),
                new Vector(0,90,0), new Vector(0.75, 0.75, 0.75), "hand_r"));
        characterLoopAnimation.start();

        // Put the object in the hand
        Onset.delay(10000, () -> {
            nearbyVehicle.setProperty("fuel", 100, true);
            CharacterManager.setCharacterFreeze(player, false);
            characterLoopAnimation.stop();
        });
        return true;
    }
}
