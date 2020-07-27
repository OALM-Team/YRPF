package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.manager.JobManager;
import fr.yuki.yrpf.manager.UIStateManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class AddJobExpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        Player playerTarget = Onset.getPlayers().stream().filter(x -> x.getId() == Integer.parseInt(args[0]))
                .findFirst().orElse(null);

        if(playerTarget == null) return true;
        JobManager.addExp(playerTarget, args[1], Integer.parseInt(args[2]));
        player.sendMessage("You have granted " +args[2]+ " exp of " + args[1]);
        return false;
    }
}
