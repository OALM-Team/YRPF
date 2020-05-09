package fr.yuki.YukiRPFramework.manager;

import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.character.CharacterStyle;
import fr.yuki.YukiRPFramework.dao.AccountDAO;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.net.payload.StyleSavePartPayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;

public class CharacterManager {

    private static HashMap<String, CharacterState> characterStates;

    public static void init() {
        characterStates = new HashMap<String, CharacterState>();
    }

    public static CharacterState getCharacterStateByPlayer(Player player) {
        return characterStates.get(player.getSteamId());
    }

    /**
     * Save part of the player style
     * @param player The player
     * @param payload The payload
     */
    public static void handleStyleSavePart(Player player, StyleSavePartPayload payload) {
        Onset.print("Update style player part="+ payload.getPartType() + " value=" + payload.getValue());
        Account account = WorldManager.getPlayerAccount(player);
        CharacterStyle characterStyle = account.decodeCharacterStyle();
        switch (payload.getPartType()) {
            case "gender":
                characterStyle.setGender(payload.getValue());
                break;

            case "body":
                characterStyle.setBody(payload.getValue());
                break;

            case "hair":
                characterStyle.setHair(payload.getValue());
                break;

            case "hair_color":
                characterStyle.setHairColor(payload.getValue());
                break;

            case "top":
                characterStyle.setTop(payload.getValue());
                break;

            case "pant":
                characterStyle.setPant(payload.getValue());
                break;

            case "shoes":
                characterStyle.setShoes(payload.getValue());
                break;

            case "name":
                account.setCharacterName(payload.getValue());
                break;
        }
        account.setCharacterStyle(characterStyle);
        WorldManager.savePlayer(player);
    }

    public static void handleCharacterCustomDone(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        CharacterStyle characterStyle = account.decodeCharacterStyle();
        characterStyle.attachStyleToPlayer(player);

        player.setName(account.getCharacterName());
        player.setProperty("characterName", account.getCharacterName(), true);
        account.setCharacterCreationRequest(0);
        WorldManager.savePlayer(player);
        UIStateManager.handleUIToogle(player, "customCharacter");
    }

    /**
     * Freeze/Unfreeze a player
     * @param player The player
     * @param freeze Freeze state
     */
    public static void setCharacterFreeze(Player player, boolean freeze) {
        if(freeze) {
            player.callRemoteEvent("Character:FreezePlayer");
        } else {
            player.callRemoteEvent("Character:UnFreezePlayer");
        }
    }

    public static void onPlayerDeath(Player player, Player killer) {
        // Update state
        CharacterState characterState = getCharacterStateByPlayer(player);
        characterState.setDead(true);
        setCharacterFreeze(player, true);

        // Update account
        Account account = WorldManager.getPlayerAccount(player);
        account.setIsDead(1);
        try {
            AccountDAO.updateAccount(account, null);
        } catch (Exception ex) {
            Onset.print("Can't save account: " + ex.toString());
        }

        UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Vous êtes mort, attendez les secours ou votre mort");
        player.setRespawnTime(60000 * 5);
    }

    public static HashMap<String, CharacterState> getCharacterStates() {
        return characterStates;
    }
}
