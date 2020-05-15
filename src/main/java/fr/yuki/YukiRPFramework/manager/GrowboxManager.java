package fr.yuki.YukiRPFramework.manager;

import fr.yuki.YukiRPFramework.job.tools.GrowBox;
import fr.yuki.YukiRPFramework.model.JobTool;
import fr.yuki.YukiRPFramework.net.payload.GrowboxFillWaterPotPayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

public class GrowboxManager {
    public static void handleGrowboxFillWaterPot(Player player, GrowboxFillWaterPotPayload payload) {
        Onset.print("GrowbotId: " + payload.getGrowboxId());
        Onset.print("PotId: " + payload.getPotId());
        JobTool jobTool = JobManager.getJobTools().stream().filter
                (x -> x.getUuid().equals(payload.getGrowboxId())).findFirst().orElse(null);
        if(jobTool == null) return;
        GrowBox growBox = (GrowBox)jobTool.getJobToolHandler();
        growBox.fillPotWater(player, payload.getPotId());
    }
}
