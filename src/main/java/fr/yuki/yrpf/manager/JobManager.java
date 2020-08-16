package fr.yuki.yrpf.manager;

import com.google.gson.Gson;
import eu.bebendorf.ajorm.Repo;
import fr.yuki.yrpf.character.CharacterJobLevel;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.character.CharacterStyle;
import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.enums.JobEnum;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.job.*;
import fr.yuki.yrpf.model.*;
import fr.yuki.yrpf.net.payload.AddCharacterJobPayload;
import fr.yuki.yrpf.net.payload.AddXpBarItemPayload;
import lombok.Getter;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Color;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Pickup;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class JobManager {
    @Getter
    private static Map<String, Job> jobs;
    @Getter
    private static List<WearableWorldObject> wearableWorldObjects;
    @Getter
    private static List<JobNPC> jobNPCS;
    @Getter
    private static List<JobTool> jobTools;
    @Getter
    private static List<JobLevel> jobLevels;
    @Getter
    private static List<JobVehicleRental> jobVehicleRentals;
    @Getter
    private static DeliveryPointConfig deliveryPointConfig;
    @Getter
    private static List<JobOutfit> jobOutfits;

    public static void init() throws SQLException, IOException {
        jobNPCS = Repo.get(JobNPC.class).all();
        Onset.print("Loaded " + jobNPCS.size() + " job npc(s) from the database");

        jobOutfits = Repo.get(JobOutfit.class).all();
        Onset.print("Loaded " + jobOutfits.size() + " job outfit(s) from the database");

        jobTools = Repo.get(JobTool.class).all();
        jobTools.forEach(it -> {
            it.setJobToolType(it.getJobToolType());
        });
        Onset.print("Loaded " + jobTools.size() + " job tool(s) from the database");

        jobLevels = Repo.get(JobLevel.class).all();
        Onset.print("Loaded " + jobLevels.size() + " job level(s) from the database");

        jobVehicleRentals = Repo.get(JobVehicleRental.class).all();
        Onset.print("Loaded " + jobVehicleRentals.size() + " job vehicle(s) from the database");

        loadDeliveryPoints();

        wearableWorldObjects = new ArrayList<>();
        jobs = new LinkedHashMap<>();
        jobs.put(JobEnum.LUMBERJACK.name(), new LumberjackJob());
        jobs.put(JobEnum.GARBAGE.name(), new GarbageJob());
        jobs.put(JobEnum.DELIVERY.name(), new DeliveryJob());
        jobs.put(JobEnum.MINER.name(), new MinerJob());
        jobs.put(JobEnum.FISHER.name(), new FisherJob());
        jobs.put(JobEnum.POLICE.name(), new PoliceJob());
        jobs.put(JobEnum.WEED.name(), new WeedJob());
        jobs.put(JobEnum.EMS.name(), new EMSJob());

        spawnJobOutfitsPoint();
        spawnVehicleRentalSpawns();
        initPaycheck();
        initWorldWearableObjectExpiration();
    }

    public static void initWorldWearableObjectExpiration() {
        Onset.timer(60000, () -> {
            for(WearableWorldObject wearableWorldObject : wearableWorldObjects.stream().filter(x -> !x.isWeared() && x.getVehicleUUID().equals(""))
                    .collect(Collectors.toList())) {
                try {
                    if(wearableWorldObject.isExpired()) wearableWorldObject.deleteObject();
                }catch (Exception ex) {}
            }
        });
    }

    public static void initPaycheck() {
        int paycheckAmount = 1000;
        Onset.timer(60000 * 30, () -> {
            for(Player player : Onset.getPlayers()) {
                try {
                    Account account = WorldManager.getPlayerAccount(player);
                    AccountJobWhitelist accountJobWhitelist = AccountManager.getAccountJobWhitelists().stream()
                            .filter(x -> x.getAccountId() == account.getId())
                            .findFirst().orElse(null);
                    if(accountJobWhitelist != null){
                        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Salaire +" + paycheckAmount + "$ !");
                        account.setBankMoney(account.getBankMoney() + paycheckAmount);
                    }
                } catch (Exception ex) {}
            }
        });
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
        for(Map.Entry<String, Job> job : jobs.entrySet()) {
            if(characterJobLevels.stream().filter(x -> x.getJobId().equals(job.getKey())).findFirst().orElse(null) == null) {
                CharacterJobLevel characterJobLevel = new CharacterJobLevel();
                characterJobLevel.setJobId(job.getKey());
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
        Job job = jobs.values().stream().filter(x -> x.getJobType().equals(jobOutfit.getJobId())).findFirst().orElse(null);
        if(job == null) return false;

        // Check level and whitelist level
        if(job.isWhitelisted()) {
            AccountJobWhitelist accountJobWhitelist = AccountManager.getAccountJobWhitelists().stream()
                    .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(job.getJobType()))
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

        if(account.isInService()) {
            CharacterStyle characterStyle = account.decodeOriginalCharacterStyle();
            account.setCharacterStyle(characterStyle);
            characterStyle.attachStyleToPlayer(player);
            account.setInService(false);
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
            account.setInService(true);
        }
        SoundManager.playSound3D("sounds/zip.mp3", player.getLocation(), 500, 0.2);
        WorldManager.savePlayer(player);
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, I18n.t(account.getLang(), "toast.outfit.success_change"));

        return true;
    }

    public static int getJobLevelForPlayer(Player player, String job) {
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
        CharacterJobLevel characterJobLevel = characterJobLevels.stream().filter(x -> x.getJobId().equals(job)).findFirst().orElse(null);
        return characterJobLevel.getJobLevel().getLevel();
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
        for(Map.Entry<String, Job> job : jobs.entrySet()) {
            for(WorldHarvestObject worldHarvestObject : job.getValue().getWorldHarvestObjects()) {
                try {
                    if(worldHarvestObject.isNear(player)) {
                        if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() != null) {
                            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "action.vehicle.wearSomething"));
                            return true;
                        }


                        worldHarvestObject.harvest(player);
                        return true;
                    }
                } catch (Exception ex) {}
            }
        }
        return false;
    }

    public static void addExp(Player player, String job, int amount) {
        if(amount <= 0) return;
        amount = amount * WorldManager.getServerConfig().getXpRate();
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
        CharacterJobLevel characterJobLevel = characterJobLevels.stream().filter(x -> x.getJobId().equals(job)).findFirst().orElse(null);
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
            InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, sellListItem.getPrice(), false);
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
                InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, jobNPCListItem.getPrice(), false);
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

    public static boolean isWhitelistForThisJob(Player player, String jobType) {
        Account account = WorldManager.getPlayerAccount(player);
        Job job = jobs.values().stream().filter(x -> x.getJobType().equals(jobType)).findFirst().orElse(null);
        if(job == null) return false;
        AccountJobWhitelist accountJobWhitelist = AccountManager.getAccountJobWhitelists().stream()
                .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(job.getJobType()))
                .findFirst().orElse(null);
        if(accountJobWhitelist == null) {
            return false;
        }
        return true;
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

        Job job = jobs.values().stream().filter(x -> x.getJobType().equals(nearbyJobVehicleRental.getJobId())).findFirst().orElse(null);
        if(job == null) return false;

        // Check level and whitelist level
        if(job.isWhitelisted()) {
            AccountJobWhitelist accountJobWhitelist = AccountManager.getAccountJobWhitelists().stream()
                    .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(job.getJobType()))
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

        VehicleManager.CreateVehicleResult result = VehicleManager.createVehicle(nearbyJobVehicleRental.getVehicleModelId(),
                new Vector(nearbyJobVehicleRental.getSpawnX(), nearbyJobVehicleRental.getSpawnY(), nearbyJobVehicleRental.getSpawnZ()),
                player.getLocationAndHeading().getHeading(), player, null, true);

        java.awt.Color color = java.awt.Color.decode(nearbyJobVehicleRental.getColor());
        result.getVehicle().setColor(new Color(color.getRed(), color.getGreen(), color.getBlue()));
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, I18n.t(account.getLang(), "toast.job_vehicle.success_rental",
                String.valueOf(nearbyJobVehicleRental.getCost())));


        Onset.getServer().callLuaEvent("YRPF:JobAPI:OnVehicleRental", player.getId(),
                nearbyJobVehicleRental.getJobId(),
                nearbyJobVehicleRental.getVehicleModelId(),
                result.getVehicle().getId());
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
        House house = HouseManager.getHouseAtLocation(jobTool.getPosition());
        Account account = WorldManager.getPlayerAccount(player);
        if(house != null) {
            if(!JobManager.isWhitelistForThisJob(player, JobEnum.POLICE.name())) {
                if(!HouseManager.canBuildInHouse(player, house)) {
                    UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.house.need_to_be_inside"));
                    return;
                }
            }
        }
        jobTool.getJobToolHandler().onUse(player);
    }

    public static ArrayList<Player> getWhitelistedPlayersForJob(JobEnum jobEnum) {
        ArrayList<Player> players = new ArrayList<>();
        for(Player player : Onset.getPlayers()) {
            try {
                Account account = WorldManager.getPlayerAccount(player);
                AccountJobWhitelist accountJobWhitelist = AccountManager.getAccountJobWhitelists().stream()
                        .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(jobEnum.name()))
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

}
