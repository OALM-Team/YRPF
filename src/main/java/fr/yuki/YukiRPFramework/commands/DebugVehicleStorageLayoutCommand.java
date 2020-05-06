package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.VehicleManager;
import fr.yuki.YukiRPFramework.vehicle.storeLayout.StoreLayoutTransform;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.entity.WorldObject;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class DebugVehicleStorageLayoutCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() == null) return true;

        StoreLayoutTransform storeLayoutTransform = new StoreLayoutTransform(0, new Vector(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])),
                new Vector(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])),
                new Vector(Double.parseDouble(args[6]), Double.parseDouble(args[7]), Double.parseDouble(args[8])));

        Vehicle vehicle = VehicleManager.getNearestVehicle(player.getLocation());
        CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject()
                .storeInVehicle(vehicle, storeLayoutTransform);

        Onset.delay(10000, () -> {
            CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().getWorldObject().destroy();
        });

        return true;
    }
}
