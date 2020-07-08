package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.manager.InventoryManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.ItemTemplate;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class AddItemAllCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 5) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        for(Player other : Onset.getPlayers()) {
            try{
                Account otherAccount = WorldManager.getPlayerAccount(other);
                if(otherAccount == null) continue;
                ItemTemplate itemTemplate = InventoryManager.getItemTemplates().get(Integer.parseInt(args[0]));
                InventoryManager.addItemToPlayer(other, String.valueOf(itemTemplate.getId()), Integer.parseInt(args[1]), true);
                player.sendMessage("Added item to " + other.getName());
            }catch (Exception ex) {}
        }
        return true;
    }
}
