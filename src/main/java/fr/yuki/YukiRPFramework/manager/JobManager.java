package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.character.CharacterJobLevel;
import fr.yuki.YukiRPFramework.dao.JobLevelDAO;
import fr.yuki.YukiRPFramework.dao.JobNPCDAO;
import fr.yuki.YukiRPFramework.dao.JobToolDAO;
import fr.yuki.YukiRPFramework.dao.JobVehicleRentalDAO;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.job.*;
import fr.yuki.YukiRPFramework.model.*;
import fr.yuki.YukiRPFramework.net.payload.AddToastPayload;
import fr.yuki.YukiRPFramework.net.payload.AddXpBarItemPayload;
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

    public static void init() throws SQLException, IOException {
        jobNPCS = JobNPCDAO.loadJobNPCS();
        Onset.print("Loaded " + jobNPCS.size() + " job npc(s) from the database");

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

    /**
     * Try to find a object to harvest for the player
     * @param player The player
     */
    public static void tryToHarvest(Player player) {
        for(Map.Entry<JobEnum, Job> job : jobs.entrySet()) {
            for(WorldHarvestObject worldHarvestObject : job.getValue().getWorldHarvestObjects()) {
                if(worldHarvestObject.isNear(player)) {
                    if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() != null) {
                        UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Impossible de porter cette objet car vous portez déjà un objet");
                        return;
                    }

                    worldHarvestObject.harvest(player);
                    return;
                }
            }
        }
    }

    public static void addExp(Player player, JobEnum job, int amount) {
        if(amount <= 0) return;
        Account account = WorldManager.getPlayerAccount(player);
        ArrayList<CharacterJobLevel> characterJobLevels = account.decodeCharacterJob();
        CharacterJobLevel characterJobLevel = characterJobLevels.stream().filter(x -> x.getJobId().equals(job.type)).findFirst().orElse(null);
        if(characterJobLevel == null) return;

        JobLevel previousJobLevel = characterJobLevel.getJobLevel();
        characterJobLevel.setExp(characterJobLevel.getExp() + amount);
        account.setJobLevels(characterJobLevels);
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddXpBarItemPayload("+" + amount + " XP " + job.type)));
        JobLevel nextJobLevel = characterJobLevel.getJobLevel();
        if(previousJobLevel.getLevel() != nextJobLevel.getLevel()) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddXpBarItemPayload("Vous etes desormais " + nextJobLevel.getName())));
        }

        WorldManager.savePlayer(player);
    }

    /**
     * Request to wear a object in the world
     * @param player The player
     * @param uuid The object uuid
     */
    public static void handleWearObjectRequest(Player player, String uuid) {
        WearableWorldObject wearableWorldObject = wearableWorldObjects.stream().filter(x -> x.getUuid().equals(uuid)).findFirst().orElse(null);
        if(wearableWorldObject == null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Impossible de porter cette objet car il est introuvable");
            return;
        }
        if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() != null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Impossible de porter cette objet car vous portez déjà un objet");
            return;
        }
        wearableWorldObject.requestWear(player);
    }

    public static void handleUnwearObject(Player player) {
        if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() == null) return;

        // Try to sell it to the nearby npc
        JobNPC jobNPCNearby = getNearbyJobNPC(player);
        if(jobNPCNearby != null) {
            JobNPCListItem jobNPCListItem = jobNPCNearby.getBuyItemByWearableItem(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject());
            if(jobNPCListItem != null) {
                Onset.print("Selling item to the npc price=" + jobNPCListItem.getPrice());
                InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, jobNPCListItem.getPrice());
                CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().requestUnwear(player, true);
                jobNPCNearby.getNpc().setAnimation(Animation.THUMBSUP);
                SoundManager.playSound3D("sounds/cash_register.mp3", player.getLocation(), 200, 0.3);
                UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Vous avez vendu votre ressource pour " + jobNPCListItem.getPrice() + "$");
            } else {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Cette personne n'achète pas ce type de ressource");
            }
            return;
        }

        // Try to use a job tool nearby
        JobTool jobToolNearby = getNearbyJobTool(player);
        if(jobToolNearby != null) {
            if(!jobToolNearby.getJobToolHandler().hasLevelRequired(player)) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Vous n'avez pas le niveau requis pour cet outil");
                return;
            }

            if(jobToolNearby.getJobToolHandler().canInteract(player)) {
                Onset.print("Use job tool type="+jobToolNearby.getJobToolType());
                if(jobToolNearby.getJobToolHandler().onUnwear(player, CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject())) {
                    CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().requestUnwear(player, true);
                }
            }
            else {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Impossible d'utiliser cet outil avec cette ressource");
            }
            return;
        }

        // Unwear the item
        CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().requestUnwear(player, false);
    }

    public static void requestVehicleRental(Player player) {
        JobVehicleRental nearbyJobVehicleRental = getNearbyVehicleRental(player);
        if(nearbyJobVehicleRental == null) return;
        if(player.getVehicle() != null) return;

        // Check vehicle around
        if(VehicleManager.getNearestVehicle(player.getLocation()) != null) {
            if(VehicleManager.getNearestVehicle(player.getLocation()).getLocation().distance(player.getLocation()) < 600) {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Il y a un véhicule dans la zone d'apparition");
                return;
            }
        }

        destroyRentalVehiclesForPlayer(player);

        VehicleManager.createVehicle(nearbyJobVehicleRental.getVehicleModelId(),
                new Vector(nearbyJobVehicleRental.getX(), nearbyJobVehicleRental.getY(), nearbyJobVehicleRental.getZ()),
                player.getLocationAndHeading().getHeading(), player, null, true);
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Vous avez loué un véhicule pour " +
                nearbyJobVehicleRental.getCost() + "$");
    }

    public static void destroyRentalVehiclesForPlayer(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        for(VehicleGarage vehicleGarage : GarageManager.getVehicleGarages().stream()
                .filter(x -> x.isRental() && x.getOwner() == account.getId()).collect(Collectors.toList())) {
            vehicleGarage.destroy();
        }
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
