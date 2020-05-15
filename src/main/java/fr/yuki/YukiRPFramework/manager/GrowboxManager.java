package fr.yuki.YukiRPFramework.manager;

import fr.yuki.YukiRPFramework.job.tools.Generator;
import fr.yuki.YukiRPFramework.job.tools.GrowBox;
import fr.yuki.YukiRPFramework.model.JobTool;
import fr.yuki.YukiRPFramework.net.payload.GrowboxFillWaterPotPayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class GrowboxManager {
    public static void init() {
        Onset.timer(10000, () -> {
            tickGrow();
        });

        Onset.timer(20000, () -> {
            tickGenerator();
        });
    }

    public static void tickGrow() {
        try {
            for(JobTool jobTool : JobManager.getJobTools().stream().filter(x -> x.getJobToolType().toLowerCase().equals("growbox"))
                .collect(Collectors.toList())) {
                try {
                    GrowBox growBox = (GrowBox)jobTool.getJobToolHandler();
                    growBox.tickGrow();
                }catch (Exception ex) {
                    ex.printStackTrace();
                    Onset.print("Can't tick single grow: " + ex.toString());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Onset.print("Can't tick grow: " + ex.toString());
        }
    }

    public static void tickGenerator() {
        try {
            for(JobTool jobTool : JobManager.getJobTools().stream().filter(x -> x.getJobToolType().toLowerCase().equals("generator"))
                    .collect(Collectors.toList())) {
                try {
                    Generator generator = (Generator) jobTool.getJobToolHandler();
                    generator.tickFuel();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Onset.print("Can't tick single generator: " + ex.toString());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();;
            Onset.print("Can't tick generator: " + ex.toString());
        }
    }

    public static Generator getGeneratorNearby(Vector position, int distance) {
        for(JobTool jobTool : JobManager.getJobTools().stream().filter(x -> x.getJobToolType().toLowerCase().equals("generator"))
                .collect(Collectors.toList())) {
            Generator generator = (Generator)jobTool.getJobToolHandler();
            if(new Vector(jobTool.getX(), jobTool.getY(), jobTool.getZ()).distance(position) <= distance) {
                return generator;
            }
        }
        return null;
    }

    public static void handleGrowboxFillWaterPot(Player player, GrowboxFillWaterPotPayload payload) {
        JobTool jobTool = JobManager.getJobTools().stream().filter
                (x -> x.getUuid().equals(payload.getGrowboxId())).findFirst().orElse(null);
        if(jobTool == null) return;
        GrowBox growBox = (GrowBox)jobTool.getJobToolHandler();
        growBox.fillPotWater(player, payload.getPotId());
    }

    public static void handleGrowboxFillSeedPot(Player player, GrowboxFillWaterPotPayload payload) {
        JobTool jobTool = JobManager.getJobTools().stream().filter
                (x -> x.getUuid().equals(payload.getGrowboxId())).findFirst().orElse(null);
        if(jobTool == null) return;
        GrowBox growBox = (GrowBox)jobTool.getJobToolHandler();
        growBox.fillPotSeed(player, payload.getPotId());
    }

    public static void handleGrowboxHarvestRequest(Player player, GrowboxFillWaterPotPayload payload) {
        JobTool jobTool = JobManager.getJobTools().stream().filter
                (x -> x.getUuid().equals(payload.getGrowboxId())).findFirst().orElse(null);
        if(jobTool == null) return;
        GrowBox growBox = (GrowBox)jobTool.getJobToolHandler();
        growBox.harvestPot(player, payload.getPotId());
    }

    public static void handleGrowboxTakePotRequest(Player player, GrowboxFillWaterPotPayload payload) {
        JobTool jobTool = JobManager.getJobTools().stream().filter
                (x -> x.getUuid().equals(payload.getGrowboxId())).findFirst().orElse(null);
        if(jobTool == null) return;
        GrowBox growBox = (GrowBox)jobTool.getJobToolHandler();
        growBox.takePot(player, payload.getPotId());
    }
}
