package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class GetIdCommand implements CommandExecutor {

    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        for(Player p : Onset.getPlayers()) {
            try {
                Account account = WorldManager.getPlayerAccount(p);
                if(account != null) {
                    if(account.getCharacterName().toLowerCase().contains(strings[0].toLowerCase())) {
                        player.sendMessage(account.getCharacterName() + " -> " + p.getId() + " (" + p.getSteamId() + ")");
                    }
                }
            } catch (Exception ex) {}
        }
        return true;
    }
}
