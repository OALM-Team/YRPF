package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.manager.UIStateManager;
import fr.yuki.yrpf.manager.WorldManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class SaveAllCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 4) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }

        for(Player other : Onset.getPlayers()) {
            WorldManager.savePlayer(other);
            UIStateManager.sendNotification(other, ToastTypeEnum.WARN, "Sauvegarde de votre personnage");
        }
        player.sendMessage("Server saved");
        return true;
    }
}
