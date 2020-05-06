package fr.yuki.YukiRPFramework;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.commands.*;
import fr.yuki.YukiRPFramework.dao.AccountDAO;
import fr.yuki.YukiRPFramework.dao.InventoryDAO;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.manager.*;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.net.payload.RequestBuyVehiclePayload;
import fr.yuki.YukiRPFramework.net.payload.RequestInventoryContentPayload;
import fr.yuki.YukiRPFramework.net.payload.StyleSavePartPayload;
import fr.yuki.YukiRPFramework.ui.UIState;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.plugin.Plugin;
import net.onfirenetwork.onsetjava.plugin.event.Event;
import net.onfirenetwork.onsetjava.plugin.event.EventHandler;
import net.onfirenetwork.onsetjava.plugin.event.player.PlayerEnterVehicleEvent;
import net.onfirenetwork.onsetjava.plugin.event.player.PlayerQuitEvent;
import net.onfirenetwork.onsetjava.plugin.event.player.PlayerRemoteEvent;
import net.onfirenetwork.onsetjava.plugin.event.player.PlayerSteamAuthEvent;

import javax.swing.*;

@Plugin(name = "YukiRPFramework", author = "Yuki")
public class YukiRPFrameworkPlugin {

    public void onEnable() {
        try {
            Database.init();
            Onset.registerListener(this);
            ModdingManager.init();
            InventoryManager.init();
            CharacterManager.init();
            VehicleManager.init();
            GarageManager.init();
            SoundManager.init();
            WorldManager.init();
            JobManager.init();
            MapManager.init();

            // Register commands
            Onset.registerCommand("item", new ItemCommand());
            Onset.registerCommand("loc", new LocCommand());
            Onset.registerCommand("v", new VCommand());
            Onset.registerCommand("addgatheritem", new AddGatherItemCommand());
            Onset.registerCommand("listgatheritem", new ShowGatherItemListCommand());
            Onset.registerCommand("dct", new DebugCharacterToolCommand());
            Onset.registerCommand("dvsl", new DebugVehicleStorageLayoutCommand());

            // Register remote events
            Onset.registerRemoteEvent("GlobalUI:ToogleWindow");
            Onset.registerRemoteEvent("Inventory:RequestContent");
            Onset.registerRemoteEvent("Inventory:UseItem");
            Onset.registerRemoteEvent("Object:Interact");
            Onset.registerRemoteEvent("ATM:Deposit");
            Onset.registerRemoteEvent("ATM:Withdraw");
            Onset.registerRemoteEvent("Vehicle:RequestLockToogle");
            Onset.registerRemoteEvent("Garage:RequestVehicle");
            Onset.registerRemoteEvent("Garage:BuyVehicle");
            Onset.registerRemoteEvent("Character:Style:SavePart");
            Onset.registerRemoteEvent("Character:Style:CustomDone");
            Onset.registerRemoteEvent("Global:UIReady");
            Onset.registerRemoteEvent("Job:WearObject");
            Onset.registerRemoteEvent("Character:RequestWearFromVehicleChest");
        } catch (Exception ex) {
            Onset.print("Can't start the plugin because : " + ex.toString());
        }
    }

    @EventHandler
    public void onJoin(PlayerSteamAuthEvent evt) {
        // Create or update the account
        try {
            Onset.print("Player joining steamId=" + evt.getPlayer().getSteamId());
            Account account = AccountDAO.findAccountBySteamId(evt.getPlayer().getSteamId());

            // Create the account if doesnt exist
            if(account == null) {
                Onset.print("Create the account for the steamId=" + evt.getPlayer().getSteamId());
                account = AccountDAO.createAccount(evt.getPlayer());
                WorldManager.getAccounts().put(account.getId(), account);

                // Create the default inventory for character
                Inventory inventory = InventoryDAO.createInventory();
                Onset.print("Inventory created id=" + inventory.getId());
                inventory.setInventoryType(1);
                inventory.setCharacterId(account.getId());
                InventoryManager.getInventories().put(inventory.getId(), inventory);
                inventory.save();
            }
            else {
                evt.getPlayer().setLocation(new Vector(account.getSaveX(), account.getSaveY(), account.getSaveZ()));
            }

            // Insert the account in the cache if not exist
            if(!WorldManager.getAccounts().containsKey(account.getId())) {
                WorldManager.getAccounts().put(account.getId(), account);
            }

            // Set properties for the player
            evt.getPlayer().setProperty("accountId", account.getId(), true);
            evt.getPlayer().setProperty("uiState", new Gson().toJson(new UIState()), true);
            CharacterManager.getCharacterStates().put(evt.getPlayer().getSteamId(), new CharacterState());
        } catch (Exception e) {
            e.printStackTrace();
            evt.getPlayer().kick("There is a error for retrieving your account: " + e.toString());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        Onset.print("Player quit steamId=" + evt.getPlayer().getSteamId());
        CharacterManager.getCharacterStates().remove(evt.getPlayer().getSteamId());
        WorldManager.savePlayer(evt.getPlayer());
    }

    @EventHandler
    public void onRemoteEvent(PlayerRemoteEvent evt) {
        try {
            Onset.print("Receive remote event name=" + evt.getName());
            switch (evt.getName()) {
                case "Inventory:RequestContent":
                    InventoryManager.handleRequestContent(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            RequestInventoryContentPayload.class));
                    break;

                case "Inventory:UseItem":

                    break;

                case "GlobalUI:ToogleWindow":
                    UIStateManager.handleUIToogle(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Object:Interact":
                    WorldManager.handleInteract(evt.getPlayer());
                    break;

                case "ATM:Deposit":
                    ATMManager.handleATMDeposit(evt.getPlayer(), (int)Float.parseFloat((evt.getArgs()[0]).toString()));
                    break;

                case "ATM:Withdraw":
                    ATMManager.handleATMWithdraw(evt.getPlayer(), (int)Float.parseFloat((evt.getArgs()[0]).toString()));
                    break;

                case "Vehicle:RequestLockToogle":
                    VehicleManager.handleVehicleLockRequest(evt.getPlayer());
                    break;

                case "Garage:RequestVehicle":
                    GarageManager.handleRequestVehicle(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Garage:BuyVehicle":
                    GarageManager.handleRequestBuyVehicle(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            RequestBuyVehiclePayload.class));
                    break;

                case "Character:Style:SavePart":
                    CharacterManager.handleStyleSavePart(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            StyleSavePartPayload.class));
                    break;

                case "Character:Style:CustomDone":
                    CharacterManager.handleCharacterCustomDone(evt.getPlayer());
                    break;

                case "Global:UIReady":
                    UIStateManager.handleUIReady(evt.getPlayer());
                    break;

                case "Job:WearObject":
                    JobManager.handleWearObjectRequest(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Character:RequestWearFromVehicleChest":
                    VehicleManager.handleRequestWearFromVehicleChest(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            Onset.print(ex.toString());
        }
    }

    @EventHandler
    public void onPlayerVehicleEnter(PlayerEnterVehicleEvent evt) {
        Onset.print("Player entered in the vehicle seat="+evt.getSeat());
        VehicleManager.onPlayerEnterVehicle(evt.getPlayer(), evt.getVehicle(), evt.getSeat());
    }
}
