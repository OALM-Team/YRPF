package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.net.payload.AddToastPayload;
import fr.yuki.YukiRPFramework.net.payload.SetLangPayload;
import fr.yuki.YukiRPFramework.net.payload.SetWindowStatePayload;
import fr.yuki.YukiRPFramework.ui.UIState;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

public class UIStateManager {
    /**
     * Toogle the window requested by the player
     * @param player The player
     * @param windowType The window type requested
     */
    public static boolean handleUIToogle(Player player, String windowType) {
        Onset.print("Toogle UI type="+windowType);
        UIState uiState = new Gson().fromJson(player.getProperty("uiState").toString(), UIState.class);
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
        }
        player.setProperty("uiState", new Gson().toJson(uiState), true);
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

    public static void setLang(Player player, String lang) {
        Account account = WorldManager.getPlayerAccount(player);
        account.setLang(lang);
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetLangPayload(account.getLang())));
    }

    public static void handleUIReady(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        MapManager.setupGameMap(player);

        // Temp for beta
        Inventory inventory = InventoryManager.getMainInventory(player);
        if(inventory.getItemByType(ItemTemplateEnum.LUMBERJACK_HATCHET_1.id) == null) {
            InventoryManager.addItemToPlayer(player, ItemTemplateEnum.LUMBERJACK_HATCHET_1.id, 1);
        }
        if(inventory.getItemByType(ItemTemplateEnum.MINER_PICKAXE_1.id) == null) {
            InventoryManager.addItemToPlayer(player, ItemTemplateEnum.MINER_PICKAXE_1.id, 1);
        }
        if(inventory.getItemByType(ItemTemplateEnum.FISHING_ROD.id) == null) {
            InventoryManager.addItemToPlayer(player, ItemTemplateEnum.FISHING_ROD.id, 1);
        }

        // Set lang to the UI
        setLang(player, account.getLang());

        // Apply style to character if there is one saved
        if(account.getCharacterCreationRequest() == 0) {
            CharacterManager.setCharacterStyle(player);
        } else if(account.getCharacterCreationRequest() == 1) { // Request character creation if no style set
            UIStateManager.handleUIToogle(player, "customCharacter");
        }
    }
}
