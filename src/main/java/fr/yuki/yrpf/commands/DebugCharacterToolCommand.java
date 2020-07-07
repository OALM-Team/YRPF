package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.character.CharacterToolAnimation;
import fr.yuki.yrpf.manager.WorldManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class DebugCharacterToolCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        CharacterToolAnimation characterToolAnimation =
                new CharacterToolAnimation(Integer.parseInt(args[0]),
                        new Vector(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])),
                        new Vector(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6])),
                        new Vector(Double.parseDouble(args[7]), Double.parseDouble(args[8]), Double.parseDouble(args[9])),
                        args[10]);
        characterToolAnimation.attach(player);
        player.setAnimation(args[11]);
        Onset.delay(10000, () -> {
            characterToolAnimation.unAttach();
        });
        return true;
    }
}
