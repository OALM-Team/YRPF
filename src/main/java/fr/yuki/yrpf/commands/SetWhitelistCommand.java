package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.manager.AccountManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.AccountJobWhitelist;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class SetWhitelistCommand implements CommandExecutor {
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
        if(accountJobWhitelist != null) {
            player.sendMessage("Job already whitelisted for this player");
            return true;
        }
        accountJobWhitelist = new AccountJobWhitelist();
        accountJobWhitelist.setAccountId(account.getId());
        accountJobWhitelist.setJobId(args[1].toUpperCase());
        accountJobWhitelist.setJobLevel(Integer.parseInt(args[2]) <= 1 ? 2 : Integer.parseInt(args[2]));
        AccountManager.getAccountJobWhitelists().add(accountJobWhitelist);
        accountJobWhitelist.save();

        player.sendMessage("Job whitelisted for this player");

        WorldManager.savePlayer(playerTarget);
        return true;
    }
}
