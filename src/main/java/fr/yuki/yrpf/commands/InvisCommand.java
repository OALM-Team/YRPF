package fr.yuki.yrpf.commands;

import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.character.CharacterStyle;
import fr.yuki.yrpf.manager.CharacterManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class InvisCommand implements CommandExecutor {

    @Override
    public boolean onCommand(Player player, String s, String[] strings) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 2) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        Account account = WorldManager.getPlayerAccount(player);
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);

        if(state.isInvisible()) {
            player.sendMessage("You are now visible");
            CharacterStyle characterStyle = account.decodeOriginalCharacterStyle();
            account.setCharacterStyle(characterStyle);
            characterStyle.attachStyleToPlayer(player);
            player.setProperty("isInvisible", "false", true);
            state.setInvisible(false);
        } else {
            player.sendMessage("You are now invisible");
            CharacterStyle characterStyle = account.decodeCharacterStyle();
            characterStyle.setBody("");
            characterStyle.setHair("");
            characterStyle.setTop("");
            characterStyle.setPant("");
            characterStyle.setShoes("");
            account.setCharacterStyle(characterStyle);
            characterStyle.attachStyleToPlayer(player);
            player.setProperty("isInvisible", "true", true);
            state.setInvisible(true);
        }
        return true;
    }
}
