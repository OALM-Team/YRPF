package fr.yuki.yrpf.commands;

import eu.bebendorf.ajorm.Repo;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class UnbanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        try {
            if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
            if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
                player.sendMessage("You don't have the level required for this command");
                return true;
            }
            Account target = Repo.get(Account.class).where("steamId", args[0]).get();
            if(target == null) {
                player.sendMessage("Account not found");
                return true;
            }
            if(WorldManager.getAccounts().containsKey(target.getId())) {
                WorldManager.getAccounts().get(target.getId()).setBanned(false);
            }
            target.setBanned(false);
            target.save();
            player.sendMessage("Account " + target.getCharacterName() + " unbanned with success");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }
}
