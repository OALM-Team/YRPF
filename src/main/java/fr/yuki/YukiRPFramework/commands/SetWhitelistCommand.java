package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.dao.AccountJobWhitelistDAO;
import fr.yuki.YukiRPFramework.manager.AccountManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.AccountJobWhitelist;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

import java.sql.SQLException;

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
        try {
            AccountJobWhitelistDAO.insertAccountJobWhiteList(accountJobWhitelist);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        player.sendMessage("Job whitelisted for this player");

        WorldManager.savePlayer(playerTarget);
        return true;
    }
}
