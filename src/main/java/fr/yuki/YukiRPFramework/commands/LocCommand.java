package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.InventoryManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.ItemTemplate;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class LocCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        Location loc = player.getLocationAndHeading();
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        state.getLastLocationsRequest().add(player.getLocation());
        player.sendMessage("Current loc x=" + loc.getX() + ", y=" + loc.getY() + ", z=" + loc.getZ() + ", h=" + loc.getHeading());
        return true;
    }
}
