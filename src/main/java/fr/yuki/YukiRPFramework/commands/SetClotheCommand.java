package fr.yuki.YukiRPFramework.commands;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.character.CharacterStyle;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.CommandExecutor;

public class SetClotheCommand implements CommandExecutor {
    @Override
    public boolean onCommand(Player player, String s, String[] args) {
        if(WorldManager.getPlayerAccount(player).getAdminLevel() == 0) return false;
        if(WorldManager.getPlayerAccount(player).getCommandLevel() < 3) {
            player.sendMessage("You don't have the level required for this command");
            return true;
        }
        Player targetPlayer = Onset.getPlayers().stream().filter(x -> x.getId() == Integer.parseInt(args[0]))
                .findFirst().orElse(null);
        Account account = WorldManager.getPlayerAccount(targetPlayer);
        CharacterState state = CharacterManager.getCharacterStateByPlayer(targetPlayer);
        CharacterStyle characterStyle = account.decodeCharacterStyle();
        switch (args[1].toLowerCase().toString()) {
            case "body":
                characterStyle.setBody(args[2]);
                break;

            case "top":
                characterStyle.setTop(args[2]);
                break;

            case "pant":
                characterStyle.setPant(args[2]);
                break;

            case "shoes":
                characterStyle.setShoes(args[2]);
                break;

            case "clear":
                characterStyle.setBody("");
                characterStyle.setHair("");
                characterStyle.setTop("");
                characterStyle.setPant("");
                characterStyle.setShoes("");
                break;
        }
        account.setCharacterStyle(characterStyle);
        characterStyle.attachStyleToPlayer(targetPlayer);
        return false;
    }
}
