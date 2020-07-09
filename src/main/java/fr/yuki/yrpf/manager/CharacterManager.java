package fr.yuki.yrpf.manager;

import com.google.gson.Gson;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.character.CharacterStyle;
import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.enums.JobEnum;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.job.Job;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.net.payload.RequestThrowItemPayload;
import fr.yuki.yrpf.net.payload.SetFoodPayload;
import fr.yuki.yrpf.net.payload.StyleSavePartPayload;
import fr.yuki.yrpf.ui.GenericMenu;
import fr.yuki.yrpf.ui.GenericMenuItem;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

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

    public static void handleCharacterCustomDone(Player player, String type) {
        Account account = WorldManager.getPlayerAccount(player);
        CharacterStyle characterStyle = account.decodeCharacterStyle();
        characterStyle.attachStyleToPlayer(player);

        player.setName(account.getCharacterName());
        player.setProperty("characterName", account.getCharacterName(), true);
        account.setCharacterCreationRequest(0);
        WorldManager.savePlayer(player);
        if(type.equals("character")) {
            UIStateManager.handleUIToogle(player, "customCharacter");
        } else {
            UIStateManager.handleUIToogle(player, "customOutfit");
        }
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
        account.setDead(true);
        account.save();
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
        if(CharacterManager.getCharacterStateByPlayer(player) == null) {
            Onset.print("No state found for " + player.getSteamId());
            CharacterManager.getCharacterStates().put(player.getSteamId(), new CharacterState());
        }

        CharacterState characterState = getCharacterStateByPlayer(player);
        if(characterState.isFirstSpawn()) {
            characterState.setFirstSpawn(false);
            Onset.print("First spawn for player");
        } else {
            Account account = WorldManager.getPlayerAccount(player);
            if(account != null) {
                if(account.isDead()) {
                    UIStateManager.handleUIToogle(player, "death");
                    account.setDead(false);
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

    public static void handleCharacterInteract(Player player) {
        Player nearestPlayer = WorldManager.getNearestPlayer(player);

        if(nearestPlayer != null) {
            if(nearestPlayer.getLocation().distance(player.getLocation()) > 200) {
                nearestPlayer = null;
            }
        }

        if(nearestPlayer == null) {
            // Police Menu
            if(JobManager.isWhitelistForThisJob(player, JobEnum.POLICE.name())) {
                CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
                GenericMenu genericMenu = new GenericMenu(player);
                genericMenu.getItems().add(new GenericMenuItem("Enfoncer la porte", "window.CallEvent(\"RemoteCallInterface\"," +
                        " \"Police:KickDoor\");"));
                genericMenu.addCloseItem();
                genericMenu.show();
                characterState.setCurrentGenericMenu(genericMenu);
            }
            return;
        }

        if(nearestPlayer.getLocation().distance(player.getLocation()) < 200) {
            // Build generic menu
            CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
            GenericMenu genericMenu = new GenericMenu(player);
            genericMenu.getItems().add(new GenericMenuItem("Fouiller la personne", "window.CallEvent(\"RemoteCallInterface\"," +
                    " \"Character:InspectCharacter\", \"" + nearestPlayer.getId() + "\");"));
            genericMenu.getItems().add(new GenericMenuItem("Donner les clés de la maison", "window.CallEvent(\"RemoteCallInterface\"," +
                    " \"Character:GiveHouseKey\", \"" + nearestPlayer.getId() + "\");"));
            genericMenu.addCloseItem();
            genericMenu.show();
            characterState.setCurrentGenericMenu(genericMenu);
        }
    }

    public static HashMap<String, CharacterState> getCharacterStates() {
        return characterStates;
    }

    public static void handleGenericMenuDismiss(Player player) {
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        if(characterState.getCurrentGenericMenu() != null) {
            characterState.getCurrentGenericMenu().hide();
            characterState.setCurrentGenericMenu(null);
        }
    }

    public static void handleInspectCharacter(Player player, int inspectPlayerId) {
        handleGenericMenuDismiss(player); // Close generic menu

        Player target = Onset.getPlayer(inspectPlayerId);
        if(target == null) return;
        CharacterState targetState = CharacterManager.getCharacterStateByPlayer(target);
        if(!targetState.isCuffed()) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "La personne n'est pas fouillable");
            return;
        }
        Account account = WorldManager.getPlayerAccount(player);

        // Display items
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        GenericMenu genericMenu = new GenericMenu(player);
        Inventory inventory = InventoryManager.getMainInventory(target);
        for(InventoryItem item : inventory.getInventoryItems()) {
            String itemName = I18n.t(account.getLang(), "item.name." + item.getTemplateId());
            genericMenu.getItems().add(new GenericMenuItem("x" + item.getAmount() + " " + itemName, ""));
        }
        genericMenu.addCloseItem();
        genericMenu.show();
        characterState.setCurrentGenericMenu(genericMenu);
    }
}
