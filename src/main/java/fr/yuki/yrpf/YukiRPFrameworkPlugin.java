package fr.yuki.yrpf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.bebendorf.ajorm.Repo;
import fr.yuki.yrpf.discord.OnsetDiscordBot;
import fr.yuki.yrpf.luaapi.LuaAPIManager;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.commands.*;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.manager.*;
import fr.yuki.yrpf.modding.ModdingCustomModel;
import fr.yuki.yrpf.model.*;
import fr.yuki.yrpf.net.payload.*;
import fr.yuki.yrpf.utils.ServerConfig;
import fr.yuki.yrpf.world.RestrictedZone;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.data.Weapon;
import net.onfirenetwork.onsetjava.plugin.Plugin;
import net.onfirenetwork.onsetjava.plugin.event.EventHandler;
import net.onfirenetwork.onsetjava.plugin.event.player.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Plugin(name = "YukiRPFramework", author = "Yuki")
public class YukiRPFrameworkPlugin {

    private static OnsetDiscordBot onsetDiscordBot;

    public static OnsetDiscordBot getOnsetDiscordBot() {
        return onsetDiscordBot;
    }

    public void onEnable() {
        try {
            WorldManager.initServerConfig();
            I18n.init();
            Database.init();
            Onset.registerListener(this);
            ModdingManager.init();
            MapManager.init();
            InventoryManager.init();
            ItemManager.init();
            CharacterManager.init();
            VehicleManager.init();
            GarageManager.init();
            SoundManager.init();
            WorldManager.init();
            JobManager.init();
            AccountManager.init();
            GrowboxManager.init();
            FuelManager.init();
            PhoneManager.init();
            HouseManager.init();
            TimeManager.init();
            CompanyManager.init();
            TebexManager.init();
            LuaAPIManager.init();

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
            Onset.registerCommand("settime", new SetTimeCommand());
            Onset.registerCommand("getid", new GetIdCommand());
            Onset.registerCommand("setcommandlevel", new SetCommandLevelCommand());
            Onset.registerCommand("requestcreation", new RequestCharacterCreationCommand());
            Onset.registerCommand("invis", new InvisCommand());
            Onset.registerCommand("regenphone", new RegenPhoneNumberCommand());
            Onset.registerCommand("ann", new AnnCommand());
            Onset.registerCommand("itemall", new AddItemAllCommand());
            Onset.registerCommand("fgc", new ForceGarbageCommand());
            Onset.registerCommand("debugtest", new DebugTestCommand());
            Onset.registerCommand("kick", new KickCommand());
            Onset.registerCommand("setclothe", new SetClotheCommand());
            Onset.registerCommand("setcharacterscale", new SetCharacterScaleCommand());
            Onset.registerCommand("unban", new UnbanCommand());
            Onset.registerCommand("gv", new GiveVehicleCommand());
            Onset.registerCommand("unloadaccount", new UnloadAccountCommand());
            Onset.registerCommand("saveall", new SaveAllCommand());
            Onset.registerCommand("addjobexp", new AddJobExpCommand());
            Onset.registerCommand("removewhitelist", new RemoveWhitelistCommand());

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
            Onset.registerRemoteEvent("Phone:RequestCall");
            Onset.registerRemoteEvent("Phone:RequestAnswer");
            Onset.registerRemoteEvent("Phone:RequestEndCall");
            Onset.registerRemoteEvent("Phone:RequestAttachPhone");
            Onset.registerRemoteEvent("SetPlayerEditor");
            Onset.registerRemoteEvent("Phone:RequestUrgency");
            Onset.registerRemoteEvent("Phone:RequestListUrgency");
            Onset.registerRemoteEvent("Phone:SolveUrgency");
            Onset.registerRemoteEvent("Compagny:Create");
            Onset.registerRemoteEvent("Compagny:InviteEmployee");
            Onset.registerRemoteEvent("Compagny:AcceptInvitation");
            Onset.registerRemoteEvent("Compagny:DeclineInvitation");
            Onset.registerRemoteEvent("Compagny:KickEmployee");
            Onset.registerRemoteEvent("Character:Interact");
            Onset.registerRemoteEvent("Character:InspectCharacter");
            Onset.registerRemoteEvent("GenericMenu:Dismiss");
            Onset.registerRemoteEvent("Character:GiveHouseKey");
            Onset.registerRemoteEvent("Character:RevokeHouseKey");
            Onset.registerRemoteEvent("Chest:TransfertToChest");
            Onset.registerRemoteEvent("Chest:TransfertToInventory");
            Onset.registerRemoteEvent("Police:KickDoor");
            Onset.registerRemoteEvent("Vehicle:OnHornCasted");
            Onset.registerRemoteEvent("Character:HandsUp");
            Onset.registerRemoteEvent("Phone:DeleteContact");
            Onset.registerRemoteEvent("WUI:RequestSync");

            // Init discord bot
            if(!WorldManager.getServerConfig().getDiscordBotToken().equals("")) {
                onsetDiscordBot = new OnsetDiscordBot(WorldManager.getServerConfig().getDiscordBotToken());
                Onset.timer(60000 * 15, () -> {
                    onsetDiscordBot.sendMessage("Il y a actuellement **" + Onset.getPlayers().size() + "** joueur(s) en ligne");
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Onset.print("Can't start the plugin because : " + ex.toString());
        }
    }

    @EventHandler
    public void onJoin(PlayerSteamAuthEvent evt) {
        // Create or update the account
        try {
            Onset.print("Player joining steamId=" + evt.getPlayer().getSteamId());
            Account account = Repo.get(Account.class).where("steamId", evt.getPlayer().getSteamId()).get();
            ServerConfig serverConfig = WorldManager.getServerConfig();

            // Clear state
            CharacterState state = CharacterManager.getCharacterStateByPlayer(evt.getPlayer());
            if(state != null) {
                state.setCurrentPhoneCall(null);
                state.setCurrentObjectPlacementInstance(null);
                state.setCuffed(false);
            }

            // Create the account if doesnt exist
            if(account == null) {
                Onset.print("Create the account for the steamId=" + evt.getPlayer().getSteamId());

                account = new Account();
                account.setSteamName(evt.getPlayer().getName());
                account.setSteamId(evt.getPlayer().getSteamId());
                account.setPhoneNumber(PhoneManager.generateRandomPhoneNumber());
                account.setSaveX(serverConfig.getSpawnPointX());
                account.setSaveY(serverConfig.getSpawnPointY());
                account.setSaveZ(serverConfig.getSpawnPointZ());
                account.setSaveH(serverConfig.getSpawnPointH());
                account.save();
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
                Inventory inventory = new Inventory();
                inventory.save();
                Onset.print("Inventory created id=" + inventory.getId());
                inventory.setInventoryType(1);
                inventory.setCharacterId(account.getId());
                InventoryManager.getInventories().put(inventory.getId(), inventory);
                inventory.save();

                // Set properties for the player
                evt.getPlayer().setProperty("accountId", account.getId(), true);
            }
            else {
                if(account.isBanned()) {
                    evt.getPlayer().kick("You have been banned, bye bye");
                    return;
                }

                // Set properties for the player
                evt.getPlayer().setProperty("accountId", account.getId(), true);

               // evt.getPlayer().setLocation(new Vector(account.getSaveX(), account.getSaveY(), account.getSaveZ()));
                CharacterManager.teleportWithLevelLoading(evt.getPlayer(), new Location(account.getSaveX(),
                        account.getSaveY(),
                        account.getSaveZ() + 50,
                        account.getSaveH()));

                if(account.getCharacterName().equals("Unknown")) { // Recovery
                    account.setCharacterCreationRequest(1);
                    account.setPhoneNumber(PhoneManager.generateRandomPhoneNumber());
                    account.setBankMoney(4000);
                    Onset.print("Phone number generated : " + account.getPhoneNumber());

                    evt.getPlayer().setSpawnLocation(new Vector(serverConfig.getSpawnPointX(),
                            serverConfig.getSpawnPointY(), serverConfig.getSpawnPointZ()), 0);
                    CharacterManager.teleportWithLevelLoading(evt.getPlayer(), new Location(serverConfig.getSpawnPointX(),
                            serverConfig.getSpawnPointY(), serverConfig.getSpawnPointZ(), serverConfig.getSpawnPointH()));
                } else {
                    evt.getPlayer().setSpawnLocation(new Vector(account.getSaveX(), account.getSaveY(), account.getSaveZ()), 0);
                    //Onset.broadcast("<span color=\"#ffee00\">" + account.getCharacterName() + " est de retour !</>");
                }

                if(account.getOriginalStyle().equals("")) {
                    account.setOriginalStyle(account.getCharacterStyle());
                }
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
                account.save();
                Onset.print("Phone number generated : " + account.getPhoneNumber());
            }

            // Attach bag
            if(account.getBagId() != -1) {
                Account finalAccount = account;
                Bag bag = ItemManager.bags.stream().filter(x -> x.getModelId() == finalAccount.getBagId())
                        .findFirst().orElse(null);
                state.attachBag(bag, evt.getPlayer());
            }

            // If the player is dead
            if(account.getIsDead() == 1) {
                Onset.print("Player is dead");
                evt.getPlayer().setHealth(0);
            } else {
                Onset.print("Set player health to " + account.getHealth());
                evt.getPlayer().setHealth(account.getHealth());
            }

            // Set current time
            TimeManager.setCurrentHourForPlayer(evt.getPlayer());

            // Set modding objects
            for(ModdingCustomModel customModel : ModdingManager.getModdingFile().getCustomModels()) {
                evt.getPlayer().callRemoteEvent("Modding:AddCustomObject", customModel.getId(), customModel.getPath());
            }
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
        CharacterState state = CharacterManager.getCharacterStateByPlayer(evt.getPlayer());
        if(state != null) {
            if(state.getCurrentPhoneAttached() != null) {
                state.getCurrentPhoneAttached().unAttach();
            }
            if(state.getCurrentMask() != null) {
                state.unattachMask();
            }
            if(state.getWearableWorldObject() != null) {
                state.getWearableWorldObject().deleteObject();
            }
            if(state.getCurrentPhoneCall() != null ){
                state.getCurrentPhoneCall().end();
            }
            CharacterManager.getCharacterStates().remove(evt.getPlayer().getSteamId());
        }
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
                    WorldManager.handleInteract(evt.getPlayer(), evt.getArgs().length == 0 ? 0 : Integer.parseInt((evt.getArgs()[0]).toString()), evt.getArgs().length == 0 ? 0 : Integer.parseInt((evt.getArgs()[1]).toString()));
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
                    CharacterManager.handleCharacterCustomDone(evt.getPlayer(), (evt.getArgs()[0]).toString());
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

                case "Phone:RequestCall":
                    PhoneManager.handleRequestCall(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Phone:RequestAnswer":
                    PhoneManager.handleCallAnswer(evt.getPlayer());
                    break;

                case "Phone:RequestEndCall":
                    PhoneManager.handleCallEnd(evt.getPlayer());
                    break;

                case "Phone:RequestAttachPhone":
                    PhoneManager.handleAttachPhone(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "SetPlayerEditor":
                    ModdingManager.onEditorOpen(evt.getPlayer());
                    break;

                case "Phone:RequestUrgency":
                    PhoneManager.handleUrgencyRequest(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            UrgencyRequestPayload.class));
                    break;

                case "Phone:RequestListUrgency":
                    PhoneManager.handleUrgencyListRequest(evt.getPlayer());
                    break;

                case "Phone:SolveUrgency":
                    PhoneManager.handleUrgencySolveRequest(evt.getPlayer(), new Gson().fromJson((evt.getArgs()[0]).toString(),
                            SolveUrgencyPayload.class));
                    break;

                case "Compagny:Create":
                    CompanyManager.handleCreateRequest(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Compagny:InviteEmployee":
                    CompanyManager.handleInviteEmployee(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Compagny:AcceptInvitation":
                    CompanyManager.handleAcceptInvitation(evt.getPlayer());
                    break;

                case "Compagny:DeclineInvitation":
                    CompanyManager.handleDeclineInvitation(evt.getPlayer());
                    break;

                case "Compagny:KickEmployee":
                    CompanyManager.handleKickEmployee(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Character:Interact":
                    CharacterManager.handleCharacterInteract(evt.getPlayer());
                    break;

                case "GenericMenu:Dismiss":
                    CharacterManager.handleGenericMenuDismiss(evt.getPlayer());
                    break;

                case "Character:InspectCharacter":
                    CharacterManager.handleInspectCharacter(evt.getPlayer(), Integer.parseInt((evt.getArgs()[0]).toString()));
                    break;

                case "Character:GiveHouseKey":
                    HouseManager.handleGiveHouseKey(evt.getPlayer(), Integer.parseInt((evt.getArgs()[0]).toString()));
                    break;

                case "Character:RevokeHouseKey":
                    HouseManager.handleRevokeKeys(evt.getPlayer(), Integer.parseInt((evt.getArgs()[0]).toString()));
                    break;

                case "Chest:TransfertToChest":
                    InventoryManager.handleTransfertToChest(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Chest:TransfertToInventory":
                    InventoryManager.handleTransfertToInventory(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "Police:KickDoor":
                    PoliceManager.handleKickDoor(evt.getPlayer());
                    break;

                case "Vehicle:OnHornCasted":
                    VehicleManager.handleVehicleHorn(evt.getPlayer());
                    break;

                case "Character:HandsUp":
                    CharacterManager.handleHandsup(evt.getPlayer());
                    break;

                case "Phone:DeleteContact":
                    PhoneManager.handleDeletePhoneContact(evt.getPlayer(), (evt.getArgs()[0]).toString());
                    break;

                case "WUI:RequestSync":
                    WorldUIManager.handleRequestSync(evt.getPlayer(), Integer.parseInt((evt.getArgs()[0]).toString()));
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

        // Check Restricted Zone
        for(RestrictedZone restrictedZone : WorldManager.getRestrictedZones()) {
            if(!restrictedZone.canInteractWithDoor(evt.getPlayer(), evt.getDoor())) {
                UIStateManager.sendNotification(evt.getPlayer(), ToastTypeEnum.ERROR, "Vous n'avez pas le droit d'intéragir avec cette porte");
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
            String color = "ff0000";
            String name = "Staff";
            switch (account.getCommandLevel()) {
                case 1:
                    color = "984fff";
                    name = "Helpeur";
                    break;

                case 2:
                    color = "0c42f2";
                    name = "Modérateur";
                    break;

                case 3:
                    color = "577eff";
                    name = "Modérateur+";
                    break;

                case 4:
                    color = "fc0097";
                    name = "Administrateur";
                    break;

                case 5:
                    color = "00ff2a";
                    name = "Fondateur";
                    break;
            }
            Onset.broadcast("<span color=\"#" + color + "\">[" + name + "] " + account.getCharacterName() + "(" + evt.getPlayer().getId() + ")</>: "
                    + evt.getMessage());
        } else {
            Onset.broadcast("<span color=\"#ffae00\">[Citoyen] " + account.getCharacterName() + "(" + evt.getPlayer().getId() + ")</>: "
                + evt.getMessage());
        }
    }
}
