package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.luaapi.genericmenu.AddGenericMenuItemEF;
import fr.yuki.yrpf.luaapi.genericmenu.CreateGenericMenuEF;
import fr.yuki.yrpf.luaapi.genericmenu.ShowGenericMenuEF;
import fr.yuki.yrpf.luaapi.items.CreateItemTemplateEF;
import fr.yuki.yrpf.luaapi.items.GetItemQuantityEF;
import fr.yuki.yrpf.luaapi.items.RemoveItemEF;
import fr.yuki.yrpf.luaapi.job.*;
import fr.yuki.yrpf.luaapi.map.AddMapMarkerEF;
import fr.yuki.yrpf.luaapi.map.RemoveMapMarkerEF;
import fr.yuki.yrpf.luaapi.vehicle.GetWorldVehicleByPlayerEF;
import fr.yuki.yrpf.luaapi.vehicle.PersistVehicleEF;
import fr.yuki.yrpf.luaapi.vehicle.SetVehicleItemChestSizeEF;
import net.onfirenetwork.onsetjava.Onset;

public class LuaAPIManager {
    public static void init() {
        Onset.getServer().addFunctionExport("GetAdminLevel", new GetAdminLevelEF());
        Onset.getServer().addFunctionExport("GetAccount", new GetAccountEF());
        Onset.getServer().addFunctionExport("AddAmbiantSound", new AddAmbiantSoundEF());
        Onset.getServer().addFunctionExport("SendToast", new SendToastEF());
        Onset.getServer().addFunctionExport("AddRestrictedZone", new AddRestrictedZoneEF());
        Onset.getServer().addFunctionExport("AddI18nKey", new AddI18nKeyEF());
        Onset.getServer().addFunctionExport("GetI18nForPlayer", new GetI18NKeyForPlayerEF());

        // Job API
        Onset.getServer().addFunctionExport("CreateJob", new CreateJobEF());
        Onset.getServer().addFunctionExport("AddJobLevel", new AddJobLevelEF());
        Onset.getServer().addFunctionExport("AddJobResource", new AddJobResourceEF());
        Onset.getServer().addFunctionExport("AddItemResourceRequirement", new AddItemResourceRequirementEF());
        Onset.getServer().addFunctionExport("SetHarvestAnimation", new SetHarvestAnimationEF());
        Onset.getServer().addFunctionExport("IsJobWhitelisted", new IsJobWhitelistedEF());

        // Item API
        Onset.getServer().addFunctionExport("CreateItemTemplate", new CreateItemTemplateEF());
        Onset.getServer().addFunctionExport("RemoveItem", new RemoveItemEF());
        Onset.getServer().addFunctionExport("AddItem", new AddItemEF());
        Onset.getServer().addFunctionExport("GetItemQuantity", new GetItemQuantityEF());

        // Generic Menu API
        Onset.getServer().addFunctionExport("CreateMenu", new CreateGenericMenuEF());
        Onset.getServer().addFunctionExport("AddMenuItem", new AddGenericMenuItemEF());
        Onset.getServer().addFunctionExport("ShowMenu", new ShowGenericMenuEF());

        // Map API
        Onset.getServer().addFunctionExport("AddMapMarker", new AddMapMarkerEF());
        Onset.getServer().addFunctionExport("RemoveMapMarker", new RemoveMapMarkerEF());

        // Vehicle
        Onset.getServer().addFunctionExport("PersistVehicleInstance", new PersistVehicleEF());
        Onset.getServer().addFunctionExport("GetWorldVehicleByPlayer", new GetWorldVehicleByPlayerEF());
        Onset.getServer().addFunctionExport("SetVehicleItemChestSize", new SetVehicleItemChestSizeEF());
    }
}
