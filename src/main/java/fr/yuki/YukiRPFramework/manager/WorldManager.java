package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.dao.*;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.job.DeliveryPointConfig;
import fr.yuki.YukiRPFramework.job.ObjectPlacementInstance;
import fr.yuki.YukiRPFramework.model.*;
import fr.yuki.YukiRPFramework.net.payload.AddSellerItemPayload;
import fr.yuki.YukiRPFramework.net.payload.AddToastPayload;
import fr.yuki.YukiRPFramework.net.payload.BuySellItemRequestPayload;
import fr.yuki.YukiRPFramework.net.payload.SetWindowStatePayload;
import fr.yuki.YukiRPFramework.ui.UIState;
import fr.yuki.YukiRPFramework.utils.ServerConfig;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.NPC;
import net.onfirenetwork.onsetjava.entity.Pickup;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class WorldManager {
    private static ServerConfig serverConfig;
    private static HashMap<Integer, Account> accounts;
    private static ArrayList<ATM> atms;
    private static ArrayList<Garage> garages;
    private static ArrayList<VehicleSeller> vehicleSellers;
    private static ArrayList<GroundItem> groundItems;
    private static ArrayList<Seller> sellers;

    /**
     * Init the world manager
     * @throws SQLException
     */
    public static void init() throws SQLException, IOException {
        accounts = new HashMap<>();
        groundItems = new ArrayList<>();

        // Load atms
        atms = ATMDAO.loadATMs();
        spawnATMs();
        Onset.print("Loaded " + atms.size() + " atm(s) from the database");

        // Load garages
        garages = GarageDAO.loadGarages();
        spawnGarages();
        Onset.print("Loaded " + garages.size() + " garage(s) from the database");

        // Load vehicle sellers
        vehicleSellers = VehicleSellerDAO.loadVehicleSellers();
        Onset.print("Loaded " + vehicleSellers.size() + " vehicle seller(s) from the database");
        spawnVehicleSellers();

        // Load sellers
        sellers = SellerDAO.loadSellers();
        Onset.print("Loaded " + sellers.size() + " seller(s) from the database");
        spawnSellers();

    }

    public static void initServerConfig() throws IOException {
        new File("yrpf").mkdir();
        if(new File("yrpf/server_config.json").exists()) {
            serverConfig = new Gson().fromJson(new FileReader("yrpf/server_config.json"), ServerConfig.class);
        } else {
            serverConfig = new ServerConfig();
            new File("yrpf/server_config.json").createNewFile();
            FileWriter fileWriter = new FileWriter("yrpf/server_config.json");
            fileWriter.write(new Gson().toJson(serverConfig));
            fileWriter.close();
        }
    }

    /**
     * Spanw atms in the world
     */
    private static void spawnATMs() {
        for(ATM atm : atms) {
            try  {
                Pickup pickup = Onset.getServer().createPickup(new Vector(atm.getX(), atm.getY(), atm.getZ()-100), 336);
                pickup.setScale(new Vector(1,1,0.1d));
                pickup.setProperty("color", "02e630", true);
                atm.setPickup(pickup);
                Onset.getServer().createText3D("ATM [" + I18n.t(WorldManager.getServerConfig().getServerLanguage(), "ui.common.use") + "]", 15, atm.getX(), atm.getY(), atm.getZ() + 150, 0 , 0 ,0);
            }
            catch(Exception ex) {
                Onset.print("Can't spawn the atm: " + ex.toString());
            }
        }
    }

    /**
     * Spawns garages in the world
     */
    private static void spawnGarages() {
        for(Garage garage : garages) {
            Pickup pickup = Onset.getServer().createPickup(new Vector(garage.getX(), garage.getY(), garage.getZ()-100), 336);
            pickup.setScale(new Vector(3,3,0.1d));
            pickup.setProperty("color", "6500bd", true);
            Onset.getServer().createText3D("Garage [" + I18n.t(WorldManager.getServerConfig().getServerLanguage(), "ui.common.use") + "]", 20, garage.getX(), garage.getY(), garage.getZ() + 150, 0 , 0 ,0);
        }
    }

    private static void spawnVehicleSellers() {
        for(VehicleSeller vehicleSeller : vehicleSellers) {
            Pickup pickup = Onset.getServer().createPickup(new Vector(vehicleSeller.getX(), vehicleSeller.getY(),
                    vehicleSeller.getZ()-100), 336);
            pickup.setScale(new Vector(1,1,0.1d));
            pickup.setProperty("color", "ffa600", true);
            Onset.getServer().createText3D("Vendeur " + vehicleSeller.getName() + " [" + I18n.t(WorldManager.getServerConfig().getServerLanguage(), "ui.common.use") + "]", 20, vehicleSeller.getX(),
                    vehicleSeller.getY(), vehicleSeller.getZ() + 150, 0 , 0 ,0);
            NPC npc = Onset.getServer().createNPC(new Location(vehicleSeller.getX(), vehicleSeller.getY(),
                    vehicleSeller.getZ(), vehicleSeller.getH()));
            npc.setRespawnTime(1);
            npc.setHealth(999999);
            npc.setProperty("clothing", vehicleSeller.getNpcClothing(), true);
        }
    }

    public static void spawnSellers() {
        for(Seller seller : sellers) {
            Onset.getServer().createText3D(seller.getName() + " [" + I18n.t(WorldManager.getServerConfig().getServerLanguage(), "ui.common.use") + "]", 20, seller.getX(),
                    seller.getY(), seller.getZ() + 150, 0 , 0 ,0);
            NPC npc = Onset.getServer().createNPC(new Location(seller.getX(), seller.getY(),
                    seller.getZ(), seller.getH()));
            npc.setRespawnTime(1);
            npc.setHealth(999999);
            npc.setProperty("clothing", seller.getNpcClothing(), true);
        }
    }

    /**
     * Find a player by the steam id
     * @param steamId The steam id
     * @return The player
     */
    public static Player findPlayerBySteamId(final String steamId) {
        for(Player p : Onset.getPlayers()) {
            if(p.getSteamId() == steamId) return p;
        }
        return null;
    }

    /**
     * Find a player by the account id
     * @param accountId The account id
     * @return The player
     */
    public static Player findPlayerByAccountId(int accountId) {
        for (Player p : Onset.getPlayers()) {
            if(CharacterManager.getCharacterStateByPlayer(p) == null) continue;
            if(p.getProperty("accountId") == null) continue;
            if(p.getPropertyInt("accountId") == accountId) {
                return p;
            }
        }
        return null;
    }

    /**
     * Handle the interaction request and send it to all interactible elements
     * @param player The player
     */
    public static void handleInteract(Player player) {
        if(CharacterManager.getCharacterStateByPlayer(player).isDead()) {
            return;
        }

        // Check weared object
        if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() != null && player.getVehicle() == null) {
            JobManager.handleUnwearObject(player);
            return;
        }

        if(ATMManager.handleATMInteract(player)) return;
        if(GarageManager.handleVSellerInteract(player)) return;
        if(player.getVehicle() == null)
            if(handlePickupGroundItem(player)) return;
        if(JobManager.tryToHarvest(player)) return;
        if(player.getVehicle() == null) {
            if(VehicleManager.handleVehicleChestStorageRequest(player)) return;
        }
        if(GarageManager.handleGarageInteract(player)) return;
        if(player.getVehicle() == null)
            if(JobManager.requestVehicleRental(player)) return;
        if(player.getVehicle() == null)
            if(JobManager.handleSellJobNpcInventoryItem(player)) return;
        if(player.getVehicle() == null) if(JobManager.handleJobOutfitRequest(player)) return;
        if(player.getVehicle() == null) if(handleSellerInteract(player)) return;
    }

    /**
     * Save the player account (position, inventory ..)
     * @param player The player
     */
    public static void savePlayer(Player player) {
        try {
            Account account = getPlayerAccount(player);
            AccountDAO.updateAccount(account, player);
        } catch (Exception ex) {
            Onset.print("Can't save the account: " + ex.toString());
        }
    }

    public static GroundItem getNearestGroundItem(Vector position) {
        GroundItem nearestGroundItem = null;
        for(GroundItem groundItem : groundItems) {
            if(nearestGroundItem == null) {
                if(groundItem.getPosition().distance(position) < 150) {
                    nearestGroundItem = groundItem;
                }
            }
            else {
                if(groundItem.getPosition().distance(position) < nearestGroundItem.getPosition().distance(position)) {
                    nearestGroundItem = groundItem;
                }
            }
        }
        return nearestGroundItem;
    }

    public static boolean handlePickupGroundItem(Player player) {
        GroundItem groundItem = getNearestGroundItem(player.getLocation());
        if(groundItem == null) return false;
        groundItem.pickByPlayer(player);
        return true;
    }

    public static boolean handleSellerInteract(Player player) {
        for(Seller seller : sellers) {
            if(seller.isNear(player)) {
                openSeller(player, seller);
                return true;
            }
        }
        return false;
    }

    public static Seller getNearbySeller(Player player) {
        for(Seller seller : sellers) {
            if(seller.isNear(player)) {
                return seller;
            }
        }
        return null;
    }

    public static void openSeller(Player player, Seller seller) {
        if(!UIStateManager.handleUIToogle(player, "seller")) {
            CharacterManager.setCharacterFreeze(player, false);
            return;
        }

        CharacterManager.setCharacterFreeze(player, true);
        for(SellerItem sellerItem : seller.decodeItems()) {
            ItemTemplate itemTemplate = InventoryManager.getItemTemplates().get(sellerItem.getId());
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddSellerItemPayload
                    (String.valueOf(sellerItem.getId()), itemTemplate.getName(), sellerItem.getPrice())));
        }
    }

    public static void handleBuySellItemSeller(Player player, BuySellItemRequestPayload payload) {
        Seller seller = getNearbySeller(player);
        if(seller == null) return;
        SellerItem sellerItem = seller.decodeItems().stream().filter(x -> x.getId() == Integer.parseInt(payload.getId()))
                .findFirst().orElse(null);
        if(sellerItem == null) return;
        if(payload.getQuantity() <= 0) return;

        Account account = WorldManager.getPlayerAccount(player);

        ItemTemplate itemTemplate = InventoryManager.getItemTemplates().get(sellerItem.getId());
        Inventory inventory = InventoryManager.getMainInventory(player);
        if(sellerItem.getPrice() > 0) {
            // Buy
            int totalPrice = sellerItem.getPrice() * payload.getQuantity();
            if(inventory.getCashAmount() < totalPrice) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR,
                        I18n.t(account.getLang(), "toast.seller.no_enought_money_on_me", String.valueOf(payload.getQuantity()),itemTemplate.getName()));
                return;
            }
            if(InventoryManager.addItemToPlayer(player, String.valueOf(itemTemplate.getId()), payload.getQuantity()) == null) {
                return;
            }
            inventory.removeItem(inventory.getItemByType(ItemTemplateEnum.CASH.id), totalPrice);
            UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, I18n.t(account.getLang(), "toast.seller.success_buy", String.valueOf(payload.getQuantity()),itemTemplate.getName()));
        } else {
            // Sell
            InventoryItem inventoryItem = inventory.getItemByType(payload.getId());
            if(inventoryItem == null) return;
            if(inventoryItem.getAmount() < payload.getQuantity()) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR,
                        I18n.t(account.getLang(), "toast.seller.no_enought_item_on_me", String.valueOf(payload.getQuantity()),itemTemplate.getName()));
                return;
            }
            int totalPrice = (-(sellerItem.getPrice())) * payload.getQuantity();
            inventory.removeItem(inventoryItem, payload.getQuantity());
            InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, totalPrice);
            UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS,
                    I18n.t(account.getLang(), "toast.seller.sell_success", String.valueOf(payload.getQuantity()),itemTemplate.getName()));
        }
        WorldManager.savePlayer(player);
    }

    public static void handleObjectRequestPlacement(Player player) {
        UIStateManager.handleUIToogle(player, "statewindow");
    }

    public static void handleObjectEditPlacementCancel(Player player) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state == null) return;
        if(state.getCurrentObjectPlacementInstance() == null) {
            return;
        }

        UIStateManager.handleUIToogle(player, "statewindow");
        Account account = WorldManager.getPlayerAccount(player);
        ObjectPlacementInstance objectPlacementInstance = state.getCurrentObjectPlacementInstance();
        objectPlacementInstance.destroy();
        state.setCurrentObjectPlacementInstance(null);
        Onset.print("Cancel placement of the object instance modelId="+objectPlacementInstance.getModelId()
                + " uuid=" + objectPlacementInstance.getUuid());
        UIStateManager.sendNotification(player, ToastTypeEnum.WARN, I18n.t(account.getLang(), "toast.placement.cancel_placement"));
    }

    public static void handleObjectPlacementDone(Player player, Vector position, Vector rotation) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state == null) return;
        if(state.getCurrentObjectPlacementInstance() == null) {
            Onset.print("Can't cancel placement, there is instance for the player in the state");
            return;
        }

        // Remove interface
        UIState uiState = new Gson().fromJson(player.getProperty("uiState").toString(), UIState.class);
        uiState.setStatewindow(false);
        player.setProperty("uiState", new Gson().toJson(uiState), true);
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetWindowStatePayload
                ("statewindow", uiState.isStatewindow())));

        //UIStateManager.handleUIToogle(player, "statewindow");
        Account account = WorldManager.getPlayerAccount(player);
        ObjectPlacementInstance objectPlacementInstance = state.getCurrentObjectPlacementInstance();
        objectPlacementInstance.destroy();
        state.setCurrentObjectPlacementInstance(null);
        objectPlacementInstance.onPlacementDone(player, position, rotation);
    }


    /**
     * Get the account for the player
     * @param player The player
     * @return The account
     */
    public static Account getPlayerAccount(Player player) {
        return accounts.get(player.getPropertyInt("accountId"));
    }

    public static ArrayList<ATM> getAtms() {
        return atms;
    }

    public static HashMap<Integer, Account> getAccounts() {
        return accounts;
    }

    public static ArrayList<Garage> getGarages() {
        return garages;
    }

    public static ArrayList<VehicleSeller> getVehicleSellers() {
        return vehicleSellers;
    }

    public static ServerConfig getServerConfig() {
        return serverConfig;
    }

    public static ArrayList<GroundItem> getGroundItems() {
        return groundItems;
    }
}
