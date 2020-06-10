package fr.yuki.YukiRPFramework;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.commands.*;
import fr.yuki.YukiRPFramework.dao.AccountDAO;
import fr.yuki.YukiRPFramework.dao.InventoryDAO;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.manager.*;
import fr.yuki.YukiRPFramework.model.*;
import fr.yuki.YukiRPFramework.net.payload.*;
import fr.yuki.YukiRPFramework.tebex.TebexAPI;
import fr.yuki.YukiRPFramework.utils.ServerConfig;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.data.Weapon;
import net.onfirenetwork.onsetjava.plugin.Plugin;
import net.onfirenetwork.onsetjava.plugin.event.EventHandler;
import net.onfirenetwork.onsetjava.plugin.event.player.*;

import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Plugin(name = "YukiRPFramework", author = "Yuki")
public class YukiRPFrameworkPlugin {

    public void onEnable() {
        try {
            WorldManager.initServerConfig();
            I18n.init();
            Database.init();
            Onset.registerListener(this);
            ModdingManager.init();
            InventoryManager.init();
            ItemManager.init();
            CharacterManager.init();
            VehicleManager.init();
            GarageManager.init();
            SoundManager.init();
            WorldManager.init();
            JobManager.init();
            AccountManager.init();
            MapManager.init();
            GrowboxManager.init();
            FuelManager.init();
            PhoneManager.init();
            HouseManager.init();
            TimeManager.init();
            TebexManager.init();

            // Register commands
            Onset.registerCommand("item", new ItemCommand());
            Onset.registerCommand("loc", new LocCommand());
            Onset.registerCommand("v", new VCommand());
            Onset.registerCommand("vmove", new MoveCommand());
            Onset.registerCommand("dv", new DVCommand());
            Onset.registerCommand("addgatheritem", new AddGatherItemCommand());
            Onset.registerCommand("listgatheritem", new ShowGatherItemListCommand());
            Onset.registerCommand("dct", new DebugCharacterToolCommand());
            Onset.registerCommand("dvsl", new DebugVehicleStorageLayoutCommand());
            Onset.registerCommand("adddelivery", new AddDeliveryPointCommand());
            Onset.registerCommand("setadmin", new SetAdminLevelCommand());
            Onset.registerCommand("ban", new BanCommand());
            Onset.registerCommand("goto", new GotoCommand());
            Onset.registerCommand("bring", new BringCommand());
            Onset.registerCommand("flip", new FlipCommand());
            Onset.registerCommand("lang", new SetLangCommand());
            Onset.registerCommand("dop", new DebugObjectPlacementCommand());
            Onset.registerCommand("setwhitelist", new SetWhitelistCommand());
            Onset.registerCommand("cuff", new CuffCommand());
            Onset.registerCommand("revive", new ReviveCommand());
            Onset.registerCommand("nitro", new NitroCommand());
            Onset.registerCommand("dhouse", new DebugHouseCommand());
            Onset.registerCommand("chouse", new CreateHouseCommand());
            Onset.registerCommand("fhouse", new FreeHouseCommand());
            Onset.registerCommand("houseprops", new SetHousePropsCommand());
            Onset.registerCommand("givehousekey", new GiveHouseKeyCommand());

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
            Onset.registerRemoteEvent("Job:CharacterJobRequest");
            Onset.registerRemoteEvent("Character:RequestWearFromVehicleChest");
            Onset.registerRemoteEvent("Inventory:ThrowItem");
            Onset.registerRemoteEvent("Seller:BuySellItem");
            Onset.registerRemoteEvent("Object:EditPlacementCancel");
            Onset.registerRemoteEvent("Object:EditPlacement");
            Onset.registerRemoteEvent("Object:EditPlacementDone");
            Onset.registerRemoteEvent("Job:UseJobTool");
            Onset.registerRemoteEvent("Growbox:FillWaterPot");
            Onset.registerRemoteEvent("Growbox:FillSeedPot");
            Onset.registerRemoteEvent("Growbox:HarvestPot");
            Onset.registerRemoteEvent("Growbox:TakePot");
            Onset.registerRemoteEvent("Phone:AddContact");
            Onset.registerRemoteEvent("Phone:RequestContacts");
            Onset.registerRemoteEvent("Phone:RequestSendMessage");
            Onset.registerRemoteEvent("Phone:RequestConversation");
            Onset.registerRemoteEvent("Phone:RequestConversationsList");
            Onset.registerRemoteEvent("House:RequestHouseMenu");
            Onset.registerRemoteEvent("House:RequestBuy");
            Onset.registerRemoteEvent("Object:EditExistingPlacement");
            Onset.registerRemoteEvent("Phone:RequestBuyItemShop");
            Onset.registerRemoteEvent("Weapon:StoreWeapon");
            Onset.registerRemoteEvent("Growbox:Destroy");
            Onset.registerRemoteEvent("ATM:GetInfos");
        } catch (Exception ex) {
            ex.printStackTrace();
            Onset.print("Can't start the plugin because : " + ex.toString());
        }
    }

    private void initTebex() throws Exception {
    }

    @EventHandler
    public void onJoin(PlayerSteamAuthEvent evt) {
        // Create or update the account
        try {
            Onset.print("Player joining steamId=" + evt.getPlayer().getSteamId());
            Account account = AccountDAO.findAccountBySteamId(evt.getPlayer().getSteamId());
            ServerConfig serverConfig = WorldManager.getServerConfig();

            // Create the account if doesnt exist
            if(account == null) {
                Onset.print("Create the account for the steamId=" + evt.getPlayer().getSteamId());
                account = AccountDAO.createAccount(evt.getPlayer());
                WorldManager.getAccounts().put(account.getId(), account);

                // Set spawn location
                evt.getPlayer().setSpawnLocation(new Vector(serverConfig.getSpawnPointX(),
                        serverConfig.getSpawnPointY(), serverConfig.getSpawnPointZ()), serverConfig.getSpawnPointH());
                evt.getPlayer().setLocation(new Vector(serverConfig.getSpawnPointX(),
                        serverConfig.getSpawnPointY(), serverConfig.getSpawnPointZ()));
                evt.getPlayer().setHeading(serverConfig.getSpawnPointH());
                CharacterManager.teleportWithLevelLoading(evt.getPlayer(), new Location(serverConfig.getSpawnPointX(),
                        serverConfig.getSpawnPointY(), serverConfig.getSpawnPointZ(), serverConfig.getSpawnPointH()));

                // Create the default inventory for character
                Inventory inventory = InventoryDAO.createInventory();
                Onset.print("Inventory created id=" + inventory.getId());
                inventory.setInventoryType(1);
                inventory.setCharacterId(account.getId());
                InventoryManager.getInventories().put(inventory.getId(), inventory);
                inventory.save();

                // Set properties for the player
                evt.getPlayer().setProperty("accountId", account.getId(), true);

                // Start money
                InventoryManager.addItemToPlayer(evt.getPlayer(), ItemTemplateEnum.CASH.id, 15000);
            }
            else {
                if(account.getIsBanned()==1) {
                    evt.getPlayer().kick("You have been banned, bye bye");
                    return;
                }

                // Set properties for the player
                evt.getPlayer().setProperty("accountId", account.getId(), true);

                evt.getPlayer().setSpawnLocation(new Vector(account.getSaveX(), account.getSaveY(), account.getSaveZ()), 0);
               // evt.getPlayer().setLocation(new Vector(account.getSaveX(), account.getSaveY(), account.getSaveZ()));
                CharacterManager.teleportWithLevelLoading(evt.getPlayer(), new Location(account.getSaveX(),
                        account.getSaveY(),
                        account.getSaveZ() + 50,
                        account.getSaveH()));
                Onset.broadcast("<span color=\"#ffee00\">" + account.getCharacterName() + " est de retour !</>");

                // Set weapons
                for(Map.Entry<Integer, Weapon> weaponEntry :
                        ((HashMap<Integer, Weapon>)new Gson().fromJson(account.getWeapons(), new TypeToken<HashMap<Integer, Weapon>>(){}.getType())).entrySet()) {
                    evt.getPlayer().setWeapon(weaponEntry.getKey(), weaponEntry.getValue());
                }
            }

            // Insert the account in the cache if not exist
            if(!WorldManager.getAccounts().containsKey(account.getId())) {
                WorldManager.getAccounts().put(account.getId(), account);
            }

            JobManager.initCharacterJobs(evt.getPlayer());
            if(CharacterManager.getCharacterStateByPlayer(evt.getPlayer()) == null)
                CharacterManager.getCharacterStates().put(evt.getPlayer().getSteamId(), new CharacterState());

            // Generate a phone number for the player
            if(account.getPhoneNumber().trim().equals("")) {
                account.setPhoneNumber(PhoneManager.generateRandomPhoneNumber());
                AccountDAO.updateAccount(account, null);
                Onset.print("Phone number generated : " + account.getPhoneNumber());
            }

            // Attach bag
            if(account.getBagId() != -1) {
                Account finalAccount = account;
                Bag bag = ItemManager.bags.stream().filter(x -> x.getModelId() == finalAccount.getBagId())
                        .findFirst().orElse(null);
                CharacterState state = CharacterManager.getCharacterStateByPlayer(evt.getPlayer());
                state.attachBag(bag, evt.getPlayer());
            }

            // If the player is dead
            if(account.getIsDead() == 1) {
                evt.getPlayer().setHealth(0);
            }

            // Set current time
            TimeManager.setCurrentHourForPlayer(evt.getPlayer());
        } catch (Exception e) {
            e.printStackTrace();
            evt.getPlayer().kick("There is a error for retrieving your account: " + e.toString());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        Onset.print("Player quit steamId=" + evt.getPlayer().getSteamId());
        WorldManager.handleObjectEditPlacementCancel(evt.getPlayer());
        Account account = WorldManager.getPlayerAccount(evt.getPlayer());
        Location location = evt.getPlayer().getLocationAndHeading();
        account.setSaveX(location.getX());
        account.setSaveY(location.getY());
        account.setSaveZ(location.getZ());
        account.setSaveH(location.getHeading());
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
                    ItemManager.handleItemUse(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            RequestUseItemPayload.class));
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

                case "ATM:GetInfos":
                    ATMManager.handleATMGetInfos(evt.getPlayer());
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

                case "Inventory:ThrowItem":
                    InventoryManager.handleThrowItem(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            RequestThrowItemPayload.class));
                    break;

                case "Job:CharacterJobRequest":
                    JobManager.handleRequestCharacterJobs(evt.getPlayer());
                    break;

                case "Seller:BuySellItem":
                    WorldManager.handleBuySellItemSeller(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            BuySellItemRequestPayload.class));
                    break;

                case "Object:EditPlacement":
                    WorldManager.handleObjectRequestPlacement(evt.getPlayer());
                    break;

                case "Object:EditPlacementCancel":
                    WorldManager.handleObjectEditPlacementCancel(evt.getPlayer());
                    break;

                case "Object:EditPlacementDone":
                    WorldManager.handleObjectPlacementDone(evt.getPlayer(),
                            new Vector(Double.parseDouble((evt.getArgs()[0]).toString()),
                                    Double.parseDouble((evt.getArgs()[1]).toString()), Double.parseDouble((evt.getArgs()[2]).toString())),
                            new Vector(Double.parseDouble((evt.getArgs()[3]).toString()),
                                    Double.parseDouble((evt.getArgs()[4]).toString()), Double.parseDouble((evt.getArgs()[5]).toString())));
                    break;

                case "Job:UseJobTool":
                    JobManager.handleUseJobTool(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Growbox:Destroy":
                    GrowboxManager.handleGrowboxDestroy(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Growbox:FillWaterPot":
                    GrowboxManager.handleGrowboxFillWaterPot(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            GrowboxFillWaterPotPayload.class));
                    break;

                case "Growbox:FillSeedPot":
                    GrowboxManager.handleGrowboxFillSeedPot(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            GrowboxFillWaterPotPayload.class));
                    break;

                case "Growbox:HarvestPot":
                    GrowboxManager.handleGrowboxHarvestRequest(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            GrowboxFillWaterPotPayload.class));
                    break;

                case "Growbox:TakePot":
                    GrowboxManager.handleGrowboxTakePotRequest(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            GrowboxFillWaterPotPayload.class));
                    break;

                case "Phone:RequestContacts":
                    PhoneManager.handleRequestPhoneContacts(evt.getPlayer());
                    break;

                case "Phone:AddContact":
                    PhoneManager.handleAddPhoneContact(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            PhoneAddContactPayload.class));
                    break;

                case "Phone:RequestSendMessage":
                    PhoneManager.handleRequestSendMessage(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            RequestPhoneSendMessagePayload.class));
                    break;

                case "Phone:RequestConversation":
                    PhoneManager.handleRequestConversation(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Phone:RequestConversationsList":
                    PhoneManager.handleRequestConversationsList(evt.getPlayer());
                    break;

                case "House:RequestHouseMenu":
                    HouseManager.handleHouseMenu(evt.getPlayer(), evt.getPlayer().getLocation());
                    break;

                case "House:RequestBuy":
                    HouseManager.handleBuyHouseRequest(evt.getPlayer());
                    break;

                case "Object:EditExistingPlacement":
                    WorldManager.handleEditExistingPlacement(evt.getPlayer(), Integer.parseInt((evt.getArgs()[0]).toString()));
                    break;

                case "Phone:RequestBuyItemShop":
                    HouseManager.handleRequestBuyItemShop(evt.getPlayer(), Integer.parseInt((evt.getArgs()[0]).toString()));
                    break;

                case "Weapon:StoreWeapon":
                    WeaponManager.storeWeapon(evt.getPlayer());
                    break;
            }
        }
        catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            Onset.print(sw.toString());
        }
    }

    @EventHandler
    public void onPlayerVehicleEnter(PlayerEnterVehicleEvent evt) {
        Onset.print("Player entered in the vehicle seat="+evt.getSeat());
        VehicleManager.onPlayerEnterVehicle(evt.getPlayer(), evt.getVehicle(), evt.getSeat());
    }

    @EventHandler
    public void onPlayerVehicleExit(PlayerExitVehicleEvent evt) {
        Onset.print("Player exit in the vehicle seat="+evt.getSeat());
        VehicleManager.onPlayerVehicleExit(evt.getPlayer(), evt.getVehicle(), evt.getSeat());
    }

    @EventHandler
    public void onPlayerInteractDoor(PlayerInteractDoorEvent evt) {
        House house = HouseManager.getHouseAtLocation(evt.getDoor().getLocation());
        if(house != null) {
            Account account = WorldManager.getPlayerAccount(evt.getPlayer());
            if(house.getAccountId() == -1) {
                HouseManager.handleHouseMenu(evt.getPlayer(), evt.getDoor().getLocation());
                return;
            }
            if(!HouseManager.canBuildInHouse(evt.getPlayer(), house) && house.isLocked()) {
                SoundManager.playSound3D("sounds/toctoc.mp3", evt.getPlayer().getLocation(), 800, 1);
                UIStateManager.sendNotification(evt.getPlayer(), ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.house.dont_own_it"));
                return;
            }
        }
        if(VehicleManager.getNearestVehicle(evt.getPlayer().getLocation()) != null) {
            if(VehicleManager.getNearestVehicle(evt.getPlayer().getLocation())
                    .getLocation().distance(evt.getPlayer().getLocation()) < VehicleManager.getInteractionDistance(VehicleManager.getNearestVehicle(evt.getPlayer().getLocation()))) {
                return;
            } else {

                evt.getDoor().setOpen(evt.getDoor().isOpen() ? false : true);
            }
        } else {
            evt.getDoor().setOpen(evt.getDoor().isOpen() ? false : true);
        }
    }

    @EventHandler
    public void onPlayerDead(PlayerDeathEvent evt) {
        Account account = WorldManager.getPlayerAccount(evt.getPlayer());
        Location location = evt.getPlayer().getLocationAndHeading();
        account.setSaveX(location.getX());
        account.setSaveY(location.getY());
        account.setSaveZ(location.getZ());
        account.setSaveH(location.getHeading());

        CharacterManager.onPlayerDeath(evt.getPlayer(), evt.getKiller());
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnEvent evt) {
        Onset.print("Player spawn steamid="+evt.getPlayer().getSteamId());
        CharacterManager.onPlayerSpawn(evt.getPlayer());
    }

    @EventHandler
    public void onPlayerChatMessage(PlayerChatEvent evt) {
        Onset.print("Player chat message="+evt.getMessage());
        Account account = WorldManager.getPlayerAccount(evt.getPlayer());
        if(account.getAdminLevel() > 0) {
            Onset.broadcast("<span color=\"#ff0000\">[Admin] " + account.getCharacterName() + "(" + evt.getPlayer().getId() + ")</>: "
                    + evt.getMessage());
        } else {
            Onset.broadcast("<span color=\"#ffae00\">[Citoyen] " + account.getCharacterName() + "(" + evt.getPlayer().getId() + ")</>: "
                + evt.getMessage());
        }
    }
}
