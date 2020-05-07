package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.net.payload.AddToastPayload;
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

    public static void handleUIReady(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        MapManager.setupGameMap(player);

        // Apply style to character if there is one saved
        if(account.getCharacterCreationRequest() == 0) {
            account.decodeCharacterStyle().attachStyleToPlayer(player);
            player.setProperty("characterName", account.getCharacterName(), true);
            player.setName(account.getCharacterName());
        } else if(account.getCharacterCreationRequest() == 1) { // Request character creation if no style set
            UIStateManager.handleUIToogle(player, "customCharacter");
        }
    }
}
