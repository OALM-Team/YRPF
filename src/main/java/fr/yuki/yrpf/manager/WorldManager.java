package fr.yuki.yrpf.manager;

import com.google.gson.Gson;
import eu.bebendorf.ajorm.Repo;
import fr.yuki.yrpf.character.CharacterJobLevel;
import fr.yuki.yrpf.character.CharacterLoopAnimation;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.job.Job;
import fr.yuki.yrpf.job.ObjectPlacementInstance;
import fr.yuki.yrpf.job.placementObject.GenericPlacementInstance;
import fr.yuki.yrpf.modding.WorldParticle;
import fr.yuki.yrpf.model.*;
import fr.yuki.yrpf.net.payload.AddSellerItemPayload;
import fr.yuki.yrpf.net.payload.BuySellItemRequestPayload;
import fr.yuki.yrpf.utils.ServerConfig;
import fr.yuki.yrpf.world.RestrictedZone;
import lombok.Getter;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.*;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class WorldManager {
    @Getter
    private static ServerConfig serverConfig;
    @Getter
    private static Map<Integer, Account> accounts;
    @Getter
    private static List<ATM> atms;
    @Getter
    private static List<Garage> garages;
    @Getter
    private static List<VehicleSeller> vehicleSellers;
    @Getter
    private static List<GroundItem> groundItems;
    @Getter
    private static List<Seller> sellers;
    @Getter
    private static List<RestrictedZone> restrictedZones;
    @Getter
    private static List<OutfitPoint> outfitPoints;
    @Getter
    private static HashMap<Integer, WorldParticle> worldParticleHashMap = new HashMap<>();

    /**
     * Init the world manager
     * @throws SQLException
     */
    public static void init() throws SQLException, IOException {
        accounts = new HashMap<>();
        groundItems = new ArrayList<>();
        restrictedZones = new ArrayList<>();

        // Load atms
        atms = Repo.get(ATM.class).all();
        spawnATMs();
        Onset.print("Loaded " + atms.size() + " atm(s) from the database");

        // Load garages
        garages = Repo.get(Garage.class).all();
        spawnGarages();
        Onset.print("Loaded " + garages.size() + " garage(s) from the database");

        // Load vehicle sellers
        vehicleSellers = Repo.get(VehicleSeller.class).all();
        Onset.print("Loaded " + vehicleSellers.size() + " vehicle seller(s) from the database");
        spawnVehicleSellers();

        // Load sellers
        sellers = Repo.get(Seller.class).all();
        Onset.print("Loaded " + sellers.size() + " seller(s) from the database");
        spawnSellers();

        // Load outfit points
        outfitPoints = Repo.get(OutfitPoint.class).all();
        Onset.print("Loaded " + outfitPoints.size() + " outfit point(s) from the database");
        spawnOutfitPoints();

        Onset.timer(60000 * 5, () -> {
            saveAll();
        });
    }

    public static void saveAll() {
        Onset.print("Autosave players ..");
        for (Player player : Onset.getPlayers()) {
            try {
                savePlayer(player);
            }catch (Exception ex) {}
        }
        Onset.print("Autosave done");
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

    private static void spawnOutfitPoints() {
        for(OutfitPoint outfitPoint : outfitPoints) {
            try  {
                Pickup pickup = Onset.getServer().createPickup(new Vector(outfitPoint.getX(), outfitPoint.getY(), outfitPoint.getZ()-100), 336);
                pickup.setScale(new Vector(1,1,0.1d));
                pickup.setProperty("color", "5314b8", true);
                Onset.getServer().createText3D("Changer de style [" + I18n.t(WorldManager.getServerConfig().getServerLanguage(), "ui.common.use") + "]", 15, outfitPoint.getX(), outfitPoint.getY(), outfitPoint.getZ() + 150, 0 , 0 ,0);
            }
            catch(Exception ex) {
                Onset.print("Can't spawn the atm: " + ex.toString());
            }
        }
    }

    /**
     * Spanw atms in the world
     */
    private static void spawnATMs() {
        for(ATM atm : atms) {
            try  {
                atm.setCanBeRob(true); // is a world atm so it can be rob
                Pickup pickup = Onset.getServer().createPickup(new Vector(atm.getX(), atm.getY(), atm.getZ()-100), 336);
                pickup.setScale(new Vector(1,1,0.1d));
                pickup.setProperty("color", "02e630", true);
                atm.setPickup(pickup);
                //Onset.getServer().createText3D("ATM [" + I18n.t(WorldManager.getServerConfig().getServerLanguage(), "ui.common.use") + "]", 15, atm.getX(), atm.getY(), atm.getZ() + 150, 0 , 0 ,0);

                GameMapMarker mapMarker = new GameMapMarker();
                mapMarker.setType("atm");
                mapMarker.setIcon("icon_atm");
                mapMarker.setPosition(new ArrayList<>());
                mapMarker.getPosition().add((int)atm.getY());
                mapMarker.getPosition().add((int)atm.getX());
                MapManager.getMapConfig().getMarkers().add(mapMarker);

                Pickup pickup2 = Onset.getServer().createPickup(new Vector(atm.getX(), atm.getY(),
                        atm.getZ() + 150), 50070);
                pickup2.setScale(new Vector(0.5, 0.5, 0.5));
                ModdingManager.assignCustomModel(pickup2, 50070);
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
            //Onset.getServer().createText3D(seller.getName() + " [" + I18n.t(WorldManager.getServerConfig().getServerLanguage(), "ui.common.use") + "]", 20, seller.getX(),
            //        seller.getY(), seller.getZ() + 150, 0 , 0 ,0);
            NPC npc = Onset.getServer().createNPC(new Location(seller.getX(), seller.getY(),
                    seller.getZ(), seller.getH()));
            npc.setRespawnTime(1);
            npc.setHealth(999999);
            npc.setProperty("clothing", seller.getNpcClothing(), true);
            Pickup pickup = Onset.getServer().createPickup(new Vector(seller.getX(), seller.getY(),
                    seller.getZ() + 150), 50070);
            pickup.setScale(new Vector(0.5, 0.5, 0.5));
            ModdingManager.assignCustomModel(pickup, 50070);
        }
    }

    /**
     * Find a player by the steam id
     * @param steamId The steam id
     * @return The player
     */
    public static Player findPlayerBySteamId(final String steamId) {
        for(Player p : Onset.getPlayers()) {
            if(p.getSteamId().equals(steamId)) return p;
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
            try {
                if(CharacterManager.getCharacterStateByPlayer(p) == null) continue;
                if(p.getProperty("accountId") == null) continue;
                if(p.getPropertyInt("accountId") == accountId) {
                    return p;
                }
            }
            catch (Exception ex) {}
        }
        return null;
    }

    /**
     * Handle the interaction request and send it to all interactible elements
     * @param player The player
     */
    public static void handleInteract(Player player, int hitType, int hitId) {
        try {
            if(CharacterManager.getCharacterStateByPlayer(player).isDead()) {
                return;
            }

            CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
            if(!state.canInteract()) return;

            // Check weared object
            if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() != null && player.getVehicle() == null) {
                JobManager.handleUnwearObject(player, hitType, hitId);
                return;
            }

            if(player.getVehicle() == null)
                if(handlePickupGroundItem(player)) return;
            if(ATMManager.handleATMInteract(player)) return;
            if(handleOutfitPointsInteract(player)) return;
            if(GarageManager.handleVSellerInteract(player)) return;
            if(FuelManager.interactFuelPoint(player, false)) return;
            if(JobManager.tryToHarvest(player)) return;
            if(player.getVehicle() == null) {
                if(hitType == 3) {
                    if(VehicleManager.handleVehicleChestStorageRequest(player, hitId)) return;
                }
            }
            if(GarageManager.handleGarageInteract(player)) return;
            if(player.getVehicle() == null){
                if(JobManager.requestVehicleRental(player)) return;
            } else {
                if(JobManager.getNearbyVehicleRental(player) != null) {
                    JobManager.destroyRentalVehiclesForPlayer(player);
                    return;
                }
            }
            if(player.getVehicle() == null)
                if(JobManager.handleSellJobNpcInventoryItem(player)) return;
            if(player.getVehicle() == null) if(JobManager.handleJobOutfitRequest(player)) return;
            if(player.getVehicle() == null) if(handleSellerInteract(player)) return;
            if(player.getVehicle() == null) {
                if(HouseManager.handleInteractWithHouseItems(player)) return;
            }
            if(state.getCurrentChest() != null) {
                if(state.getUiState().isTransfertInventory()) {
                    if(!UIStateManager.handleUIToogle(player, "transfertInventory")) {
                        state.setCurrentChest(null);
                        return;
                    }
                }
            }

            // If nothing has been done
            Onset.getServer().callLuaEvent("Object:AfterInteract", player.getId(), hitType, hitId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean handleOutfitPointsInteract(Player player) {
        OutfitPoint outfitPoint = getNearbyOutfitPoint(player);
        if(outfitPoint == null) return false;
        UIStateManager.handleUIToogle(player, "customOutfit");
        return true;
    }

    /**
     * Save the player account (position, inventory ..)
     * @param player The player
     */
    public static void savePlayer(Player player) {
        Account account = getPlayerAccount(player);
        account.save(player);
    }

    public static GroundItem getNearestGroundItem(Vector position) {
        GroundItem nearestGroundItem = null;
        for(GroundItem groundItem : groundItems) {
            try {
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
            }catch (Exception ex) {
                continue;
            }
        }
        return nearestGroundItem;
    }

    public static boolean handlePickupGroundItem(Player player) {
        try {
            GroundItem groundItem = getNearestGroundItem(player.getLocation());
            if(groundItem == null) return false;
            CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
            if(!characterState.canInteract()) return false;
            groundItem.pickByPlayer(player);
            return true;
        } catch (Exception ex) {
            Onset.print("Can't pick up the object on the ground: " + ex.toString());
            return false;
        }
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

    public static OutfitPoint getNearbyOutfitPoint(Player player) {
        for(OutfitPoint outfitPoint : outfitPoints) {
            if(outfitPoint.isNear(player)) {
                return outfitPoint;
            }
        }
        return null;
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
        if(seller.getName().equals("Dealer")) {
            if(TimeManager.getCurrentHour() > 7 && TimeManager.getCurrentHour() < 20) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Ce vendeur est seulement disponible la nuit");
                return;
            }
        }

        // Check job
        if(!seller.getJobRequired().equals("")) {
            Account account = WorldManager.getPlayerAccount(player);
            Job job = JobManager.getJobs().values().stream().filter(x -> x.getJobType().equals(seller.getJobRequired())).findFirst().orElse(null);
            if(job == null) return;

            // Check level and whitelist level
            if(job.isWhitelisted()) {
                AccountJobWhitelist accountJobWhitelist = AccountManager.getAccountJobWhitelists().stream()
                        .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(job.getJobType()))
                        .findFirst().orElse(null);
                if(accountJobWhitelist == null) {
                    UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.job.not_whitelisted"));
                    return;
                }
                if(accountJobWhitelist.getJobLevel() < seller.getJobLevelRequired()) {
                    UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.job.not_whitelisted"));
                    return;
                }
            } else {
                ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
                CharacterJobLevel characterJobLevel = characterJobLevels.stream().filter(x -> x.getJobId().equals(seller.getJobRequired())).findFirst().orElse(null);
                if(characterJobLevel == null) return;
                if(characterJobLevel.getJobLevel().getLevel() < seller.getJobLevelRequired()) {
                    UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.job.not_whitelisted"));
                    return;
                }
            }
        }

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
        if(sellerItem.getPrice() >= 0) {
            // Buy
            int totalPrice = sellerItem.getPrice() * payload.getQuantity();
            if(inventory.getCashAmount() < totalPrice) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR,
                        I18n.t(account.getLang(), "toast.seller.no_enought_money_on_me", String.valueOf(payload.getQuantity()),itemTemplate.getName()));
                return;
            }
            if(InventoryManager.addItemToPlayer(player, String.valueOf(itemTemplate.getId()), payload.getQuantity(), true) == null) {
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
            InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, totalPrice, false);
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
        Account account = WorldManager.getPlayerAccount(player);

        if(state.getUiState().isStatewindow()) UIStateManager.handleUIToogle(player, "statewindow");
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
        House house = HouseManager.getHouseAtLocation(position);
        Account account = WorldManager.getPlayerAccount(player);
        if(house == null) {
            handleObjectEditPlacementCancel(player);
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.house.need_to_be_inside"));
            return;
        }
        if(!HouseManager.canBuildInHouse(player, house)) {
            handleObjectEditPlacementCancel(player);
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.house.need_to_be_inside"));
            return;
        }

        // Remove interface
        if(state.getUiState().isStatewindow()) UIStateManager.handleUIToogle(player, "statewindow");
        ObjectPlacementInstance objectPlacementInstance = state.getCurrentObjectPlacementInstance();
        objectPlacementInstance.destroy();
        state.setCurrentObjectPlacementInstance(null);
        objectPlacementInstance.onPlacementDone(player, position, rotation);
    }

    public static void handleEditExistingPlacement(Player player, int houseItemId) throws SQLException {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state == null) return;
        if(state.getCurrentObjectPlacementInstance() != null) {
            return;
        }
        House house = HouseManager.getHouseAtLocation(player.getLocation());
        Account account = WorldManager.getPlayerAccount(player);
        if(house == null) return;
        if(!HouseManager.canBuildInHouse(player, house))
        {
            return;
        }
        HouseItemObject houseItemObject = house.getHouseItemObjects().stream()
                .filter(x -> x.getId() == houseItemId).findFirst().orElse(null);
        if(houseItemObject == null) return;
        if(houseItemObject.getItemBehavior() != null) {
            if(!houseItemObject.getItemBehavior().canBeMove()) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Vous ne pouvez pas bouger cet objet pour le moment");
                return;
            }
        }

        houseItemObject.destroy();
        houseItemObject.delete();

        ObjectPlacementInstance objectPlacementInstance = new GenericPlacementInstance(houseItemObject.getPosition(),
                houseItemObject.getModelId(), 0);
        state.setCurrentObjectPlacementInstance(objectPlacementInstance);
        objectPlacementInstance.spawn();
        objectPlacementInstance.setEditableBy(player);
    }

    public static void cuffPlayer(Player player) {
        if(player.getVehicle() != null) return;
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);

        if(state.isCuffed()) {
            state.setCuffed(false);
            player.setAnimation(Animation.STOP);
            player.setProperty("cuffed", 0, true);
            SoundManager.playSound3D("sounds/hand_cuff.mp3", player.getLocation(), 500, 1);
        } else {
            if(!state.canInteract()) {
                return;
            }
            SoundManager.playSound3D("sounds/hand_cuff.mp3", player.getLocation(), 500, 1);
            state.setCuffed(true);
            player.setAnimation(Animation.CUFF);
            player.setProperty("cuffed", 1, true);
        }
    }

    public static void revive(Player player, Player target) {
        CharacterState targetState = CharacterManager.getCharacterStateByPlayer(target);
        if(!targetState.isDead()) return;
        CharacterLoopAnimation characterLoopAnimation = new CharacterLoopAnimation(player, Animation.REVIVE,
                5000, 2, "sounds/defibrilator.mp3");
        characterLoopAnimation.start();
        CharacterManager.setCharacterFreeze(player, true);
        CharacterManager.setCharacterFreeze(target, true);
        Onset.delay(10000, () -> {

            targetState.setDead(false);
            Account account = WorldManager.getPlayerAccount(target);
            account.setIsDead(0);
            Location location = target.getLocationAndHeading();
            account.setSaveX(location.getX());
            account.setSaveY(location.getY());
            account.setSaveZ(location.getZ());
            account.setSaveH(location.getHeading());
            target.setRespawnTime(100);

            target.setRagdoll(false);
            target.setHealth(100);
            CharacterManager.setCharacterStyle(target);
            WorldManager.savePlayer(target);
            UIStateManager.handleUIToogle(target, "death");

            characterLoopAnimation.stop();
            Onset.delay(500, () -> {
                player.setAnimation(Animation.STOP);
                target.setAnimation(Animation.PUSHUP_END);
                CharacterManager.setCharacterFreeze(player, false);
                CharacterManager.setCharacterFreeze(target, false);
            });
        });
    }

    /**
     * Get the account for the player
     * @param player The player
     * @return The account
     */
    public static Account getPlayerAccount(Player player) {
        return accounts.get(player.getPropertyInt("accountId"));
    }

    public static Player getPlayerByPhoneNumber(String phoneNumber) {
        for(Player player : Onset.getPlayers()) {
            try {
                Account account = getPlayerAccount(player);
                if(account.getPhoneNumber().equals(phoneNumber)) return player;
            }
            catch (Exception ex) {}
        }
        return null;
    }

    public static Player getNearestPlayer(Player player) {
        Player search = null;
        double currentDistance = 99999;
        for(Player p : Onset.getPlayers()) {
            try {
                if(p.getId() == player.getId()) continue;;
                if(p.getLocation().distance(player.getLocation()) < currentDistance) {
                    search = p;
                    currentDistance = p.getLocation().distance(player.getLocation());
                }
            }catch (Exception ex) {
                continue;
            }
        }
        return search;
    }

    public static Door getNearestDoor(Vector position) {
        Door search = null;
        double currentDistance = 99999;
        for(Door d : Onset.getDoors()) {
            try {
                if(d.getLocation().distance(position) < currentDistance) {
                    search = d;
                    currentDistance = d.getLocation().distance(position);
                }
            }catch (Exception ex) {
                continue;
            }
        }
        return search;
    }

}
