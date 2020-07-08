package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.manager.CharacterManager;
import fr.yuki.yrpf.manager.VehicleManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.vehicle.storeLayout.StoreLayoutTransform;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class DebugVehicleStorageLayoutCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() == null) return true;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }

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
