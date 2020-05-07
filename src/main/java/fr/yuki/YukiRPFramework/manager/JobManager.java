package fr.yuki.YukiRPFramework.manager;

import fr.yuki.YukiRPFramework.dao.JobNPCDAO;
import fr.yuki.YukiRPFramework.dao.JobToolDAO;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.job.Job;
import fr.yuki.YukiRPFramework.job.LumberjackJob;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.job.WorldHarvestObject;
import fr.yuki.YukiRPFramework.model.JobNPC;
import fr.yuki.YukiRPFramework.model.JobNPCListItem;
import fr.yuki.YukiRPFramework.model.JobTool;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.sql.SQLException;
import java.util.*;

public class JobManager {
    private static LinkedHashMap<JobEnum, Job> jobs;
    private static ArrayList<WearableWorldObject> wearableWorldObjects;
    private static ArrayList<JobNPC> jobNPCS;
    private static ArrayList<JobTool> jobTools;

    public static void init() throws SQLException {
        jobNPCS = JobNPCDAO.loadJobNPCS();
        Onset.print("Loaded " + jobNPCS.size() + " job npc(s) from the database");

        jobTools = JobToolDAO.loadJobTools();
        Onset.print("Loaded " + jobTools.size() + " job tool(s) from the database");

        wearableWorldObjects = new ArrayList<>();
        jobs = new LinkedHashMap<>();
        jobs.put(JobEnum.LUMBERJACK, new LumberjackJob());
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
            if(jobToolNearby.getJobToolHandler().canInteract(player)) {
                Onset.print("Use job tool type="+jobToolNearby.getJobToolType());
                CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().requestUnwear(player, true);
                jobToolNearby.getJobToolHandler().onUnwear(player, CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject());
            }
            else {
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Impossible d'utiliser cet outil avec cette ressource");
            }
            return;
        }

        // Unwear the item
        CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject().requestUnwear(player, false);
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
}
