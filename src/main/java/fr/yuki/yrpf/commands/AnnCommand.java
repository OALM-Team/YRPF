package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.YukiRPFrameworkPlugin;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.manager.SoundManager;
import fr.yuki.yrpf.manager.UIStateManager;
import fr.yuki.yrpf.manager.WorldManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class AnnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        for(Player other : Onset.getPlayers()) {
            try{
                UIStateManager.sendNotification(other, ToastTypeEnum.DEFAULT, "Par " + WorldManager.getPlayerAccount(player).getCharacterName() +
                        " : " + String.join(" ", strings));
                SoundManager.playSound2D(other, "notif", "sounds/success_1.mp3", 0.8);
            }catch (Exception ex) {}
        }

        if(YukiRPFrameworkPlugin.getOnsetDiscordBot() != null) {
            YukiRPFrameworkPlugin.getOnsetDiscordBot()
                    .sendMessage("**ANNONCE** Par **" + WorldManager.getPlayerAccount(player).getCharacterName() +
                            "** : " + String.join(" ", strings));
        }
        return true;
    }
}
