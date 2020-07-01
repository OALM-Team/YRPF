package fr.yuki.YukiRPFramework.commands;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.manager.InventoryManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.ItemTemplate;
import fr.yuki.YukiRPFramework.net.payload.SetWindowStatePayload;
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
        InventoryManager.addItemToPlayer(player, String.valueOf(itemTemplate.getId()), Integer.parseInt(args[1]));
        player.sendMessage("Add item " + itemTemplate.getName() + " to your inventory");
        return true;
    }
}
