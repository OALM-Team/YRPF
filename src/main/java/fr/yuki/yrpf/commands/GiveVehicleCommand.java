package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.manager.GarageManager;
import fr.yuki.yrpf.manager.InventoryManager;
import fr.yuki.yrpf.manager.VehicleManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Garage;
import fr.yuki.yrpf.model.VehicleGarage;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Color;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class GiveVehicleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 4) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }

        Player playerTarget = Onset.getPlayers().stream().filter(x -> x.getId() == Integer.parseInt(args[0]))
                .findFirst().orElse(null);
        if(playerTarget == null) return true;
        // Remove cash and spawn vehicle
        Inventory inventory = InventoryManager.getMainInventory(player);
        Garage nearestGarage = GarageManager.getNearestFromPosGarage(playerTarget);
        VehicleManager.CreateVehicleResult createVehicleResult = VehicleManager.createVehicle(Integer.parseInt(args[1]),
                playerTarget.getLocation(), playerTarget.getLocationAndHeading().getHeading(),
                playerTarget, null, false);
        java.awt.Color awtColor = java.awt.Color.decode(args[2]);
        createVehicleResult.getVehicleGarage().setColor(args[2]);
        createVehicleResult.getVehicleGarage().setGarageLastId(nearestGarage.getId());
        createVehicleResult.getVehicleGarage().save();
        createVehicleResult.getVehicle().setColor(new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue()));

        return true;
    }
}
