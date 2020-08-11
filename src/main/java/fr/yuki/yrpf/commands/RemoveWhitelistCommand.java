package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.manager.AccountManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.AccountJobWhitelist;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class RemoveWhitelistCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 2) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        Player playerTarget = Onset.getPlayers().stream().filter(x -> x.getId() == Integer.parseInt(args[0]))
                .findFirst().orElse(null);
        if(playerTarget == null) return true;
        Account account = WorldManager.getPlayerAccount(playerTarget);
        AccountJobWhitelist accountJobWhitelist = AccountManager.getAccountJobWhitelists().stream()
                .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(args[1].toUpperCase()))
                .findFirst().orElse(null);
        if(accountJobWhitelist == null) {
            player.sendMessage("The player is not whitelisted");
            return true;
        }
        AccountManager.getAccountJobWhitelists().remove(accountJobWhitelist);
        accountJobWhitelist.delete();
        player.sendMessage("Whitelist removed for " + account.getCharacterName());
        return true;
    }
}
