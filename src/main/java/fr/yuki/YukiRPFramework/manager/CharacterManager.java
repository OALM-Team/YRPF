package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.character.CharacterStyle;
import fr.yuki.YukiRPFramework.dao.AccountDAO;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.net.payload.AddToastPayload;
import fr.yuki.YukiRPFramework.net.payload.RequestThrowItemPayload;
import fr.yuki.YukiRPFramework.net.payload.SetFoodPayload;
import fr.yuki.YukiRPFramework.net.payload.StyleSavePartPayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CharacterManager {

    private static HashMap<String, CharacterState> characterStates;

    public static void init() {
        characterStates = new HashMap<String, CharacterState>();

        // Decrease food
        Onset.timer(60000 * 2, () -> {
            for(Player player : Onset.getPlayers()) {
                if(WorldManager.getPlayerAccount(player) == null) continue;
                applyFoodChange(player, -1);
            }
        });

        // Decrease drink
        Onset.timer(40000 * 2, () -> {
            for(Player player : Onset.getPlayers()) {
                if(WorldManager.getPlayerAccount(player) == null) continue;
                applyDrinkChange(player, -1);
            }
        });
    }

    public static void applyFoodChange(Player player, int value) {
        Account account = WorldManager.getPlayerAccount(player);
        if(account == null) return;
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.isDead()) return;
        account.setFoodState(account.getFoodState() + value);
        if(account.getFoodState() < 0) account.setFoodState(0);
        if(account.getFoodState() > 100) account.setFoodState(100);
        refreshFood(player);
        if(account.getFoodState() <= 0) {
            player.setHealth(player.getHealth() - 1);
        }
    }

    public static void applyDrinkChange(Player player, int value) {
        Account account = WorldManager.getPlayerAccount(player);
        if(account == null) return;
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.isDead()) return;
        account.setDrinkState(account.getDrinkState() + value);
        if(account.getDrinkState() < 0) account.setDrinkState(0);
        if(account.getDrinkState() > 100) account.setDrinkState(100);
        refreshFood(player);
        if(account.getDrinkState() <= 0) {
            player.setHealth(player.getHealth() - 1);
        }
    }

    public static CharacterState getCharacterStateByPlayer(Player player) {
        if(!characterStates.containsKey(player.getSteamId())) return null;
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
                String msg = WorldManager.getServerConfig().getWelcomeMessage();
                msg = msg.replace("%name%", account.getCharacterName());
                Onset.broadcast("<span color=\"#ffee00\">" + msg + "</>");
                break;
        }
        account.setCharacterStyle(characterStyle);
        account.setOriginalStyle(new Gson().toJson(characterStyle));
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

        if(characterState.getCurrentBag() != null) {
            characterState.unattachBag(player);
            return;
        }

        // Update account
        Account account = WorldManager.getPlayerAccount(player);
        account.setIsDead(1);
        try {
            AccountDAO.updateAccount(account, null);
        } catch (Exception ex) {
            Onset.print("Can't save account: " + ex.toString());
        }
        player.setSpawnLocation(new Vector(account.getSaveX(), account.getSaveY(), account.getSaveZ()), 0);
        UIStateManager.handleUIToogle(player, "death");
        player.setRespawnTime(WorldManager.getServerConfig().getDeathRespawnDelay());

        Inventory inventory = InventoryManager.getMainInventory(player);
        for(InventoryItem inventoryItem : inventory.getInventoryItems().stream().collect(Collectors.toList())) {
            if(inventoryItem.getTemplate().getId() == Integer.parseInt(ItemTemplateEnum.VKEY.id)) continue;
            RequestThrowItemPayload throwItemPayload = new RequestThrowItemPayload();
            throwItemPayload.setId(inventoryItem.getId());
            throwItemPayload.setQuantity(inventoryItem.getAmount());
            InventoryManager.handleThrowItem(player, throwItemPayload);
        }
        WeaponManager.clearWeapons(player);
    }

    public static void onPlayerSpawn(Player player) {
        if(CharacterManager.getCharacterStateByPlayer(player) == null)
            CharacterManager.getCharacterStates().put(player.getSteamId(), new CharacterState());

        CharacterState characterState = getCharacterStateByPlayer(player);
        if(characterState.isFirstSpawn()) {
            characterState.setFirstSpawn(false);
            Onset.print("First spawn for player");
        } else {
            Account account = WorldManager.getPlayerAccount(player);
            if(account != null) {
                if(account.getIsDead() == 1) {
                    UIStateManager.handleUIToogle(player, "death");
                    account.setIsDead(0);
                    CharacterManager.setCharacterFreeze(player, false);
                    Onset.print("Player respawned after death");
                    //UIStateManager.sendNotification(player, ToastTypeEnum.WARN,
                    //        "Après un moment a l'hôpital vous êtes sortis du coma, vous avez du mal à vous rappeler des derniers événements");
                    teleportWithLevelLoading(player, new Location(WorldManager.getServerConfig().getDeathRespawnX(),
                            WorldManager.getServerConfig().getDeathRespawnY(),
                            WorldManager.getServerConfig().getDeathRespawnZ() + 50,
                            WorldManager.getServerConfig().getDeathRespawnH()));
                    characterState.setDead(false);
                    WorldManager.savePlayer(player);
                }
                setCharacterStyle(player);
            }
        }
    }

    public static void refreshFood(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        if(account == null) return;

        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetFoodPayload(account.getFoodState(),
                account.getDrinkState())));
    }

    public static void setCharacterStyle(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        account.decodeCharacterStyle().attachStyleToPlayer(player);
        player.setProperty("characterName", account.getCharacterName(), true);
        player.setName(account.getCharacterName());
    }

    public static void teleportWithLevelLoading(Player player, Location location) {
        player.setLocationAndHeading(location);
        player.callRemoteEvent("Game:TriggerLoadLevel");
        Onset.delay(150, () -> {
            player.callRemoteEvent("Game:TriggerLoadLevel");
            player.setLocationAndHeading(location);
            player.callRemoteEvent("Game:TriggerLoadLevel");
        });
    }

    public static HashMap<String, CharacterState> getCharacterStates() {
        return characterStates;
    }
}
