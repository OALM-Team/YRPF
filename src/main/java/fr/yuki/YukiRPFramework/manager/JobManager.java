package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.character.CharacterJobLevel;
import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.character.CharacterStyle;
import fr.yuki.YukiRPFramework.dao.*;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.job.*;
import fr.yuki.YukiRPFramework.model.*;
import fr.yuki.YukiRPFramework.net.payload.AddCharacterJobPayload;
import fr.yuki.YukiRPFramework.net.payload.AddToastPayload;
import fr.yuki.YukiRPFramework.net.payload.AddXpBarItemPayload;
import fr.yuki.YukiRPFramework.ui.UIState;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Pickup;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class JobManager {
    private static LinkedHashMap<JobEnum, Job> jobs;
    private static ArrayList<WearableWorldObject> wearableWorldObjects;
    private static ArrayList<JobNPC> jobNPCS;
    private static ArrayList<JobTool> jobTools;
    private static ArrayList<JobLevel> jobLevels;
    private static ArrayList<JobVehicleRental> jobVehicleRentals;
    private static DeliveryPointConfig deliveryPointConfig;
    private static ArrayList<JobOutfit> jobOutfits;

    public static void init() throws SQLException, IOException {
        jobNPCS = JobNPCDAO.loadJobNPCS();
        Onset.print("Loaded " + jobNPCS.size() + " job npc(s) from the database");

        jobOutfits = JobOutfitDAO.loadJobOutfits();
        Onset.print("Loaded " + jobOutfits.size() + " job outfit(s) from the database");

        jobTools = JobToolDAO.loadJobTools();
        Onset.print("Loaded " + jobTools.size() + " job tool(s) from the database");

        jobLevels = JobLevelDAO.loadJobLevels();
        Onset.print("Loaded " + jobLevels.size() + " job level(s) from the database");

        jobVehicleRentals = JobVehicleRentalDAO.loadJobVehicleRental();
        Onset.print("Loaded " + jobVehicleRentals.size() + " job vehicle(s) from the database");

        loadDeliveryPoints();

        wearableWorldObjects = new ArrayList<>();
        jobs = new LinkedHashMap<>();
        jobs.put(JobEnum.LUMBERJACK, new LumberjackJob());
        jobs.put(JobEnum.GARBAGE, new GarbageJob());
        jobs.put(JobEnum.DELIVERY, new DeliveryJob());
        jobs.put(JobEnum.MINER, new MinerJob());
        jobs.put(JobEnum.FISHER, new FisherJob());
        jobs.put(JobEnum.POLICE, new PoliceJob());
        jobs.put(JobEnum.WEED, new WeedJob());
        jobs.put(JobEnum.EMS, new EMSJob());

        spawnJobOutfitsPoint();
        spawnVehicleRentalSpawns();
    }

    private static void loadDeliveryPoints() throws IOException {
        new File("yrpf").mkdir();
        if(new File("yrpf/delivery_config.json").exists()) {
            deliveryPointConfig = new Gson().fromJson(new FileReader("yrpf/delivery_config.json"), DeliveryPointConfig.class);
        } else {
            deliveryPointConfig = new DeliveryPointConfig();
            deliveryPointConfig.setPoints(new HashMap<>());
            deliveryPointConfig.getPoints().put("houses", new ArrayList<>());
            new File("yrpf/delivery_config.json").createNewFile();
            FileWriter fileWriter = new FileWriter("yrpf/delivery_config.json");
            fileWriter.write(new Gson().toJson(deliveryPointConfig));
            fileWriter.close();
        }
    }

    public static void saveDeliveryPoints() {
        try {
            FileWriter fileWriter = new FileWriter("yrpf/delivery_config.json");
            fileWriter.write(new Gson().toJson(deliveryPointConfig));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Init job levels for the character
     * @param player The player
     */
    public static void initCharacterJobs(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
        for(Map.Entry<JobEnum, Job> job : jobs.entrySet()) {
            if(characterJobLevels.stream().filter(x -> x.getJobId().equals(job.getKey().type)).findFirst().orElse(null) == null) {
                CharacterJobLevel characterJobLevel = new CharacterJobLevel();
                characterJobLevel.setJobId(job.getKey().type);
                characterJobLevel.setExp(0);
                characterJobLevels.add(characterJobLevel);
            }
        }
        account.setJobLevels(characterJobLevels);
    }

    private static void spawnVehicleRentalSpawns() {
        for(JobVehicleRental jobVehicleRental : jobVehicleRentals) {
            Pickup pickup = Onset.getServer().createPickup(new net.onfirenetwork.onsetjava.data.Vector
                    (jobVehicleRental.getX(), jobVehicleRental.getY(), jobVehicleRental.getZ()-100), 336);
            pickup.setScale(new Vector(4,4,0.3d));
            pickup.setProperty("color", "0096a3", true);
            Onset.getServer().createText3D(jobVehicleRental.getName() + " [Utiliser]", 20,
                    jobVehicleRental.getX(), jobVehicleRental.getY(), jobVehicleRental.getZ() + 150, 0 , 0 ,0);
        }
    }

    private static void spawnJobOutfitsPoint() {
        for(JobOutfit jobOutfit : jobOutfits) {
            Pickup pickup = Onset.getServer().createPickup(new net.onfirenetwork.onsetjava.data.Vector
                    (jobOutfit.getX(), jobOutfit.getY(), jobOutfit.getZ()-100), 336);
            pickup.setScale(new Vector(4,4,0.3d));
            pickup.setProperty("color", "0030a1", true);
            Onset.getServer().createText3D(jobOutfit.getName() + " [" + I18n.t(WorldManager.getServerConfig().getServerLanguage(), "ui.common.use") + "]", 17,
                    jobOutfit.getX(), jobOutfit.getY(), jobOutfit.getZ() + 150, 0 , 0 ,0);
        }
    }

    private static JobOutfit getNearbyJobOutfit(Player player) {
        for(JobOutfit jobOutfit : jobOutfits) {
            if(jobOutfit.isNear(player)) return jobOutfit;
        }
        return null;
    }

    public static boolean handleJobOutfitRequest(Player player) {
        JobOutfit jobOutfit = getNearbyJobOutfit(player);
        if(jobOutfit == null) return false;
        Account account = WorldManager.getPlayerAccount(player);
        Job job = jobs.values().stream().filter(x -> x.getJobType().type.equals(jobOutfit.getJobId())).findFirst().orElse(null);
        if(job == null) return false;

        // Check level and whitelist level
        if(job.isWhitelisted()) {
            AccountJobWhitelist accountJobWhitelist = AccountManager.getAccountJobWhitelists().stream()
                    .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(job.getJobType().type))
                    .findFirst().orElse(null);
            if(accountJobWhitelist == null) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.outfit.level_required"));
                return true;
            }
            if(accountJobWhitelist.getJobLevel() < jobOutfit.getLevelRequired()) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.outfit.level_required"));
                return true;
            }
        } else {
            ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
            CharacterJobLevel characterJobLevel = characterJobLevels.stream().filter(x -> x.getJobId().equals(jobOutfit.getJobId())).findFirst().orElse(null);
            if(characterJobLevel == null) return false;
            if(characterJobLevel.getJobLevel().getLevel() < jobOutfit.getLevelRequired()) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.outfit.level_required"));
                return true;
            }
        }

        if(account.getIsInService() == 1) {
            CharacterStyle characterStyle = account.decodeOriginalCharacterStyle();
            account.setCharacterStyle(characterStyle);
            characterStyle.attachStyleToPlayer(player);
            account.setIsInService(0);
        } else {
            // Apply the outfit
            CharacterStyle characterStyle = account.decodeCharacterStyle();
            for(JobOutfitItem jobOutfitItem : jobOutfit.decodeOutfit()) {
                switch (jobOutfitItem.getType().toLowerCase()) {
                    case "top":
                        characterStyle.setTop(jobOutfitItem.getValue());
                        break;

                    case "pant":
                        characterStyle.setPant(jobOutfitItem.getValue());
                        break;

                    case "shoes":
                        characterStyle.setShoes(jobOutfitItem.getValue());
                        break;
                }
            }
            account.setCharacterStyle(characterStyle);
            characterStyle.attachStyleToPlayer(player);
            account.setIsInService(1);
        }
        SoundManager.playSound3D("sounds/zip.mp3", player.getLocation(), 500, 0.2);
        WorldManager.savePlayer(player);
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, I18n.t(account.getLang(), "toast.outfit.success_change"));

        return true;
    }

    /**
     * Try to find a object to harvest for the player
     * @param player The player
     */
    public static boolean tryToHarvest(Player player) {
        if(player.getVehicle() != null) return false;

        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(!state.canInteract()) return false;

        Account account = WorldManager.getPlayerAccount(player);
        for(Map.Entry<JobEnum, Job> job : jobs.entrySet()) {
            for(WorldHarvestObject worldHarvestObject : job.getValue().getWorldHarvestObjects()) {
                if(worldHarvestObject.isNear(player)) {
                    if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() != null) {
                        UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.wearSomething"));
                        return true;
                    }

                    worldHarvestObject.harvest(player);
                    return true;
                }
            }
        }
        return false;
    }

    public static void addExp(Player player, JobEnum job, int amount) {
        if(amount <= 0) return;
        amount = amount * WorldManager.getServerConfig().getXpRate();
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
        CharacterJobLevel characterJobLevel = characterJobLevels.stream().filter(x -> x.getJobId().equals(job.type)).findFirst().orElse(null);
        if(characterJobLevel == null) return;

        JobLevel previousJobLevel = characterJobLevel.getJobLevel();
        characterJobLevel.setExp(characterJobLevel.getExp() + amount);
        account.setJobLevels(characterJobLevels);
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddXpBarItemPayload("+" + amount + " XP " +
                I18n.t(account.getLang(), "ui.characterJob.jobLevel_" + characterJobLevel.getJobLevel().getTranslateName()))));
        JobLevel nextJobLevel = characterJobLevel.getJobLevel();
        if(previousJobLevel.getLevel() != nextJobLevel.getLevel()) {
            SoundManager.playSound3D("sounds/success_1.mp3", player.getLocation(), 200, 1);
            String translatedJobName = I18n.t(account.getLang(), "ui.characterJob.jobLevel_" + characterJobLevel.getJobLevel().getTranslateName());
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddXpBarItemPayload(
                    I18n.t(account.getLang(), "toast.xp.level_up", translatedJobName))));
        }

        WorldManager.savePlayer(player);
    }

    /**
     * Request to wear a object in the world
     * @param player The player
     * @param uuid The object uuid
     */
    public static void handleWearObjectRequest(Player player, String uuid) {
        Account account = WorldManager.getPlayerAccount(player);
        WearableWorldObject wearableWorldObject = wearableWorldObjects.stream().filter(x -> x.getUuid().equals(uuid)).findFirst().orElse(null);
        if(wearableWorldObject == null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.wear.not_found"));
            return;
        }
        if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() != null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.wearSomething"));
            return;
        }
        wearableWorldObject.requestWear(player);
    }

    public static boolean handleSellJobNpcInventoryItem(Player player) {
        JobNPC jobNPCNearby = getNearbyJobNPC(player);
        if(jobNPCNearby == null) return false;
        Inventory inventory = InventoryManager.getMainInventory(player);
        for (JobNPCListItem sellListItem : jobNPCNearby.getBuyList()) {
            if(!sellListItem.getType().equals("item")) continue;
            InventoryItem inventoryItem = inventory.getItemByType(String.valueOf(sellListItem.getItemId()));
            if(inventoryItem == null) continue;
            Onset.print("Selling item to the npc price=" + sellListItem.getPrice());
            inventory.removeItem(inventoryItem, 1);
            SoundManager.playSound3D("sounds/cash_register.mp3", player.getLocation(), 200, 0.8);
            //UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Vous avez vendu votre " + inventoryItem.getTemplate().getName() +
            //        " pour " + sellListItem.getPrice() + "$");
            jobNPCNearby.getNpc().setAnimation(Animation.THUMBSUP);
            InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, sellListItem.getPrice());
        }
        return true;
    }

    public static void handleUnwearObject(Player player) {
        if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() == null) return;
        Account account = WorldManager.getPlayerAccount(player);

        // Try to sell it to the nearby npc
        JobNPC jobNPCNearby = getNearbyJobNPC(player);
        if(jobNPCNearby != null) {
            JobNPCListItem jobNPCListItem = jobNPCNearby.getBuyItemByWearableItem(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject());
            if(jobNPCListItem != null) {
                Onset.print("Selling item to the npc price=" + jobNPCListItem.getPrice());
                InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, jobNPCListItem.getPrice());
                CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().requestUnwear(player, true);
                jobNPCNearby.getNpc().setAnimation(Animation.THUMBSUP);
                SoundManager.playSound3D("sounds/cash_register.mp3", player.getLocation(), 200, 1);
                //UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Vous avez vendu votre ressource pour " + jobNPCListItem.getPrice() + "$");
            } else {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.npc.no_buy_kind_item"));
            }
            return;
        }

        // Try to use a job tool nearby
        JobTool jobToolNearby = getNearbyJobTool(player);
        if(jobToolNearby != null) {
            if(!jobToolNearby.getJobToolHandler().hasLevelRequired(player)) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.tool.no_level_required"));
                return;
            }

            if(jobToolNearby.getJobToolHandler().canInteract(player)) {
                Onset.print("Use job tool type="+jobToolNearby.getJobToolType());
                if(jobToolNearby.getJobToolHandler().onUnwear(player, CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject())) {
                    CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().requestUnwear(player, true);
                }
            }
            else {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.tool.no_kind_item"));
            }
            return;
        }

        // Unwear the item
        CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().requestUnwear(player, false);
    }

    public static boolean requestVehicleRental(Player player) {
        JobVehicleRental nearbyJobVehicleRental = getNearbyVehicleRental(player);
        if(nearbyJobVehicleRental == null) return false;
        if(player.getVehicle() != null) return false;

        Account account = WorldManager.getPlayerAccount(player);

        // Check vehicle around
        Vector spawnPoint = new Vector(nearbyJobVehicleRental.getSpawnX(), nearbyJobVehicleRental.getSpawnY(), nearbyJobVehicleRental.getSpawnZ());
        if(VehicleManager.getNearestVehicle(spawnPoint) != null) {
            if(VehicleManager.getNearestVehicle(spawnPoint).getLocation().distance(spawnPoint) < 600) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.garage.vehicle_block_spawn"));
                return true;
            }
        }

        Job job = jobs.values().stream().filter(x -> x.getJobType().type.equals(nearbyJobVehicleRental.getJobId())).findFirst().orElse(null);
        if(job == null) return false;

        // Check level and whitelist level
        if(job.isWhitelisted()) {
            AccountJobWhitelist accountJobWhitelist = AccountManager.getAccountJobWhitelists().stream()
                    .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(job.getJobType().type))
                    .findFirst().orElse(null);
            if(accountJobWhitelist == null) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.job.not_whitelisted"));
                return true;
            }
            if(accountJobWhitelist.getJobLevel() < nearbyJobVehicleRental.getLevelRequired()) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.job.not_whitelisted"));
                return true;
            }
        } else {
            ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
            CharacterJobLevel characterJobLevel = characterJobLevels.stream().filter(x -> x.getJobId().equals(nearbyJobVehicleRental.getJobId())).findFirst().orElse(null);
            if(characterJobLevel == null) return false;
            if(characterJobLevel.getJobLevel().getLevel() < nearbyJobVehicleRental.getLevelRequired()) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.job.not_whitelisted"));
                return true;
            }
        }

        destroyRentalVehiclesForPlayer(player);

        VehicleManager.createVehicle(nearbyJobVehicleRental.getVehicleModelId(),
                new Vector(nearbyJobVehicleRental.getSpawnX(), nearbyJobVehicleRental.getSpawnY(), nearbyJobVehicleRental.getSpawnZ()),
                player.getLocationAndHeading().getHeading(), player, null, true);
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, I18n.t(account.getLang(), "toast.job_vehicle.success_rental",
                String.valueOf(nearbyJobVehicleRental.getCost())));
        return true;
    }

    public static void destroyRentalVehiclesForPlayer(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        for(VehicleGarage vehicleGarage : GarageManager.getVehicleGarages().stream()
                .filter(x -> x.isRental() && x.getOwner() == account.getId()).collect(Collectors.toList())) {
            if(vehicleGarage.getVehicle() != null) {
                VehicleManager.clearKeysForVehicle(vehicleGarage.getVehicle(), player);
            }
            vehicleGarage.destroy();
            GarageManager.getVehicleGarages().remove(vehicleGarage);
        }
    }

    public static void handleRequestCharacterJobs(Player player) {
        if(!UIStateManager.handleUIToogle(player, "characterjob")) return;
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
        for(CharacterJobLevel characterJobLevel : characterJobLevels) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddCharacterJobPayload(characterJobLevel)));
        }
    }

    public static void handleUseJobTool(Player player, String uuid) {
        JobTool jobTool = jobTools.stream().filter(x -> x.getUuid().equals(uuid)).findFirst().orElse(null);
        if(jobTool == null) return;
        jobTool.getJobToolHandler().onUse(player);
    }

    public static ArrayList<Player> getWhitelistedPlayersForJob(JobEnum jobEnum) {
        ArrayList<Player> players = new ArrayList<>();
        for(Player player : Onset.getPlayers()) {
            try {
                Account account = WorldManager.getPlayerAccount(player);
                AccountJobWhitelist accountJobWhitelist = AccountManager.getAccountJobWhitelists().stream()
                        .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(jobEnum.type))
                        .findFirst().orElse(null);
                if(accountJobWhitelist != null) {
                    players.add(player);
                }
            }catch (Exception exception) {}
        }
        return players;
    }

    public static JobVehicleRental getNearbyVehicleRental(Player player) {
        for(JobVehicleRental jobVehicleRental : jobVehicleRentals) {
            if(jobVehicleRental.isNear(player)) return jobVehicleRental;
        }
        return null;
    }

    public static JobNPC getNearbyJobNPC(Player player) {
        for(JobNPC jobNPC : jobNPCS) {
            if(jobNPC.isNear(player)) return jobNPC;
        }
        return null;
    }

    public static JobTool getNearbyJobTool(Player player) {
        for(JobTool jobTool : jobTools) {
            if(jobTool.isNear(player)) return jobTool;
        }
        return null;
    }

    public static LinkedHashMap<JobEnum, Job> getJobs() {
        return jobs;
    }

    public static ArrayList<WearableWorldObject> getWearableWorldObjects() {
        return wearableWorldObjects;
    }

    public static ArrayList<JobNPC> getJobNPCS() {
        return jobNPCS;
    }

    public static ArrayList<JobTool> getJobTools() {
        return jobTools;
    }

    public static ArrayList<JobLevel> getJobLevels() {
        return jobLevels;
    }

    public static ArrayList<JobVehicleRental> getJobVehicleRentals() {
        return jobVehicleRentals;
    }

    public static DeliveryPointConfig getDeliveryPointConfig() {
        return deliveryPointConfig;
    }
}
