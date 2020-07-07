package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.net.payload.*;
import fr.yuki.YukiRPFramework.ui.UIState;
import fr.yuki.YukiRPFramework.utils.Basic;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.entity.Player;

import java.sql.SQLException;

public class UIStateManager {
    /**
     * Toogle the window requested by the player
     * @param player The player
     * @param windowType The window type requested
     */
    public static boolean handleUIToogle(Player player, String windowType) {
        Onset.print("Toogle UI type="+windowType);
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        UIState uiState = characterState.getUiState();
        boolean r = false;
        switch (windowType) {
            case "inventory":
                uiState.setInventory(!uiState.isInventory());
                r = uiState.isInventory();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("inventory", uiState.isInventory())));
                break;

            case "atm":
                uiState.setAtm(!uiState.isAtm());
                r = uiState.isAtm();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("atm", uiState.isAtm())));
                break;

            case "garage":
                uiState.setGarage(!uiState.isGarage());
                r = uiState.isGarage();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("garage", uiState.isGarage())));
                break;

            case "vseller":
                uiState.setVseller(!uiState.isVseller());
                r = uiState.isVseller();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("vseller", uiState.isVseller())));
                break;

            case "customCharacter":
                uiState.setCustomCharacter(!uiState.isCustomCharacter());
                r = uiState.isCustomCharacter();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("customCharacter", uiState.isCustomCharacter())));
                break;

            case "vchest":
                uiState.setVchest(!uiState.isVchest());
                r = uiState.isVchest();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("vchest", uiState.isVchest())));
                break;

            case "bigmap":
                uiState.setBigmap(!uiState.isBigmap());
                r = uiState.isBigmap();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("bigmap", uiState.isBigmap())));
                break;

            case "death":
                uiState.setDeath(!uiState.isDeath());
                r = uiState.isDeath();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("death", uiState.isDeath())));
                break;

            case "characterjob":
                uiState.setCharacterJob(!uiState.isCharacterJob());
                r = uiState.isCharacterJob();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("characterjob", uiState.isCharacterJob())));
                break;

            case "seller":
                uiState.setSeller(!uiState.isSeller());
                r = uiState.isSeller();
                if(!r) CharacterManager.setCharacterFreeze(player, false);
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("seller", uiState.isSeller())));
                break;

            case "statewindow":
                uiState.setStatewindow(!uiState.isStatewindow());
                r = uiState.isStatewindow();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("statewindow", uiState.isStatewindow())));
                break;

            case "growboxmenu":
                uiState.setGrowboxmenu(!uiState.isGrowboxmenu());
                r = uiState.isGrowboxmenu();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("growboxmenu", uiState.isGrowboxmenu())));
                break;

            case "phone":
                uiState.setPhone(!uiState.isPhone());
                r = uiState.isPhone();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("phone", uiState.isPhone())));
                break;

            case "houseBuy":
                uiState.setHouseBuy(!uiState.isHouseBuy());
                r = uiState.isHouseBuy();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("houseBuy", uiState.isHouseBuy())));
                break;

            case "genericMenu":
                uiState.setGenericMenu(!uiState.isGenericMenu());
                r = uiState.isGenericMenu();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("genericMenu", uiState.isGenericMenu())));
                break;

            case "customOutfit":
                uiState.setCustomOutfit(!uiState.isCustomOutfit());
                r = uiState.isCustomOutfit();
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                        ("customOutfit", uiState.isCustomOutfit())));
                break;

        }
        return r;
    }

    /**
     * Send a notification to the player
     * @param player The player
     * @param type The toast type
     * @param message The message
     */
    public static void sendNotification(Player player, ToastTypeEnum type, String message) {
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddToastPayload(type.type, message)));
    }

    public static void sendNotification(Player player, String type, String message) {
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddToastPayload(type, message)));
    }

    public static void setLang(Player player, String lang) {
        Account account = WorldManager.getPlayerAccount(player);
        account.setLang(lang);
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetLangPayload(account.getLang())));
    }

    public static void handleUIReady(Player player) throws SQLException {
        Account account = WorldManager.getPlayerAccount(player);
        MapManager.setupGameMap(player);

        // Temp for beta
        Inventory inventory = InventoryManager.getMainInventory(player);
        /**
        if(inventory.getItemByType(ItemTemplateEnum.LUMBERJACK_HATCHET_1.id) == null) {
            InventoryManager.addItemToPlayer(player, ItemTemplateEnum.LUMBERJACK_HATCHET_1.id, 1);
        }
        if(inventory.getItemByType(ItemTemplateEnum.MINER_PICKAXE_1.id) == null) {
            InventoryManager.addItemToPlayer(player, ItemTemplateEnum.MINER_PICKAXE_1.id, 1);
        }
        if(inventory.getItemByType(ItemTemplateEnum.FISHING_ROD.id) == null) {
            InventoryManager.addItemToPlayer(player, ItemTemplateEnum.FISHING_ROD.id, 1);
        }*/

        CharacterManager.getCharacterStateByPlayer(player).setHasUIReady(true);

        // Set lang to the UI
        setLang(player, account.getLang());
        CharacterManager.refreshFood(player);

        // Set phone number
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetPhoneNumberPayload(account.getPhoneNumber())));

        // Send compagny
        CompagnyManager.refreshCompagny(player);

        // Generate a phone number for the player
        if(account.getPhoneNumber().trim().equals("")) {
            account.setPhoneNumber(PhoneManager.generateRandomPhoneNumber());
            account.save();
            Onset.print("Phone number generated : " + account.getPhoneNumber());
        }

        // Apply style to character if there is one saved
        if(!account.isCharacterCreationRequest()) {
            CharacterManager.setCharacterStyle(player);

            if(!account.isDead()) {
                player.setRagdoll(false);
                CharacterManager.teleportWithLevelLoading(player, new Location(account.getSaveX(),
                        account.getSaveY(),
                        account.getSaveZ() + 50,
                        account.getSaveH()));
            }
            else {
                CharacterManager.teleportWithLevelLoading(player, new Location(account.getSaveX(),
                        account.getSaveY(),
                        account.getSaveZ() + 50,
                        account.getSaveH()));
                CharacterManager.setCharacterFreeze(player, true);
                Onset.delay(1500, () -> {
                    player.setRagdoll(true);
                    CharacterManager.setCharacterStyle(player);
                    player.setHealth(0);

                    // Apply interface
                    UIState uiState = new Gson().fromJson(player.getProperty("uiState").toString(), UIState.class);
                    uiState.setDeath(true);
                    player.setProperty("uiState", new Gson().toJson(uiState), true);
                    player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                            ("death", uiState.isDeath())));
                    WorldManager.savePlayer(player);
                });
            }
        } else if(account.isCharacterCreationRequest()) { // Request character creation if no style set
            CharacterManager.teleportWithLevelLoading(player, new Location(WorldManager.getServerConfig().getSpawnPointX() +  Basic.randomNumber(-30,30),
                    WorldManager.getServerConfig().getSpawnPointY() +  Basic.randomNumber(-30,30), WorldManager.getServerConfig().getSpawnPointZ(),
                    WorldManager.getServerConfig().getSpawnPointH()));
            Onset.delay(1000, () -> {
                CharacterManager.teleportWithLevelLoading(player, new Location(WorldManager.getServerConfig().getSpawnPointX() +  Basic.randomNumber(-30,30),
                        WorldManager.getServerConfig().getSpawnPointY() +  Basic.randomNumber(-30,30), WorldManager.getServerConfig().getSpawnPointZ(),
                        WorldManager.getServerConfig().getSpawnPointH()));
                Onset.delay(500, () -> {
                    UIStateManager.handleUIToogle(player, "customCharacter");
                });
            });
        }
    }
}
