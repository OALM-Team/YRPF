package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.manager.InventoryManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.ItemTemplate;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class ItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 2) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        ItemTemplate itemTemplate = InventoryManager.getItemTemplates().get(Integer.parseInt(args[0]));
        InventoryManager.addItemToPlayer(player, String.valueOf(itemTemplate.getId()), Integer.parseInt(args[1]), true);
        player.sendMessage("Add item " + itemTemplate.getName() + " to your inventory");
        return true;
    }
}
