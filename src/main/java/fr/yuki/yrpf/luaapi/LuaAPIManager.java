package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.luaapi.job.*;
import net.onfirenetwork.onsetjava.Onset;

public class LuaAPIManager {
    public static void init() {
        Onset.getServer().addFunctionExport("GetAdminLevel", new GetAdminLevelEF());
        Onset.getServer().addFunctionExport("GetAccount", new GetAccountEF());
        Onset.getServer().addFunctionExport("AddAmbiantSound", new AddAmbiantSoundEF());
        Onset.getServer().addFunctionExport("SendToast", new SendToastEF());
        Onset.getServer().addFunctionExport("AddItem", new AddItemEF());
        Onset.getServer().addFunctionExport("AddRestrictedZone", new AddRestrictedZoneEF());

        // Job API
        Onset.getServer().addFunctionExport("CreateJob", new CreateJobEF());
        Onset.getServer().addFunctionExport("AddJobLevel", new AddJobLevelEF());
        Onset.getServer().addFunctionExport("AddJobResource", new AddJobResourceEF());
        Onset.getServer().addFunctionExport("AddItemResourceRequirement", new AddItemResourceRequirementEF());
        Onset.getServer().addFunctionExport("SetHarvestAnimation", new SetHarvestAnimationEF());

    }
}
