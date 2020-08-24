package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.luaapi.genericmenu.*;
import fr.yuki.yrpf.luaapi.house.SetHouseObjectBehaviorEF;
import fr.yuki.yrpf.luaapi.inventory.*;
import fr.yuki.yrpf.luaapi.items.CreateItemTemplateEF;
import fr.yuki.yrpf.luaapi.items.GetItemQuantityEF;
import fr.yuki.yrpf.luaapi.items.RemoveItemEF;
import fr.yuki.yrpf.luaapi.job.*;
import fr.yuki.yrpf.luaapi.map.AddMapMarkerEF;
import fr.yuki.yrpf.luaapi.map.RemoveMapMarkerEF;
import fr.yuki.yrpf.luaapi.player.AddBankMoneyEF;
import fr.yuki.yrpf.luaapi.vehicle.*;
import fr.yuki.yrpf.luaapi.worldui.CreateWorldUIEF;
import fr.yuki.yrpf.luaapi.worldui.DestroyWUIEF;
import fr.yuki.yrpf.luaapi.worldui.SetImageWUIEF;
import fr.yuki.yrpf.luaapi.worldui.SetProgressWUIEF;
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
        Onset.getServer().addFunctionExport("AddImageResource", new AddImageResourceEF());
        Onset.getServer().addFunctionExport("CreateParticle", new AddParticleEF());
        Onset.getServer().addFunctionExport("DestroyParticle", new DestroyParticleEF());

        // Job API
        Onset.getServer().addFunctionExport("CreateJob", new CreateJobEF());
        Onset.getServer().addFunctionExport("AddJobLevel", new AddJobLevelEF());
        Onset.getServer().addFunctionExport("AddJobResource", new AddJobResourceEF());
        Onset.getServer().addFunctionExport("AddItemResourceRequirement", new AddItemResourceRequirementEF());
        Onset.getServer().addFunctionExport("SetHarvestAnimation", new SetHarvestAnimationEF());
        Onset.getServer().addFunctionExport("IsJobWhitelisted", new IsJobWhitelistedEF());
        Onset.getServer().addFunctionExport("SpawnWearableObject", new SpawnWearableObjectEF());
        Onset.getServer().addFunctionExport("SetWearableObjectLocation", new SetWearableObjectLocationEF());
        Onset.getServer().addFunctionExport("AddJobExp", new AddJobExpEF());
        Onset.getServer().addFunctionExport("GetWearedObject", new GetWearedObjectEF());
        Onset.getServer().addFunctionExport("GetPlayerJobLevel", new GetJobLevelEF());

        // Item API
        Onset.getServer().addFunctionExport("CreateItemTemplate", new CreateItemTemplateEF());
        Onset.getServer().addFunctionExport("RemoveItem", new RemoveItemEF());
        Onset.getServer().addFunctionExport("AddItem", new AddItemEF());
        Onset.getServer().addFunctionExport("GetItemQuantity", new GetItemQuantityEF());

        // Generic Menu API
        Onset.getServer().addFunctionExport("CreateMenu", new CreateGenericMenuEF());
        Onset.getServer().addFunctionExport("AddMenuItem", new AddGenericMenuItemEF());
        Onset.getServer().addFunctionExport("ShowMenu", new ShowGenericMenuEF());
        Onset.getServer().addFunctionExport("CloseMenu", new CloseGenericMenuEF());
        Onset.getServer().addFunctionExport("SetMenuImage", new SetMenuImageEF());

        // Map API
        Onset.getServer().addFunctionExport("AddMapMarker", new AddMapMarkerEF());
        Onset.getServer().addFunctionExport("RemoveMapMarker", new RemoveMapMarkerEF());

        // Vehicle
        Onset.getServer().addFunctionExport("PersistVehicleInstance", new PersistVehicleEF());
        Onset.getServer().addFunctionExport("GetWorldVehicleByPlayer", new GetWorldVehicleByPlayerEF());
        Onset.getServer().addFunctionExport("SetVehicleItemChestSize", new SetVehicleItemChestSizeEF());
        Onset.getServer().addFunctionExport("DeleteWearableObject", new DeleteWearableObjectEF());
        Onset.getServer().addFunctionExport("CreateVehicleStoreLayout", new CreateVehicleStoreLayoutEF());
        Onset.getServer().addFunctionExport("AddVehicleStoreLayoutItemPlacement", new AddVehicleStoreLayoutItemPlacementEF());

        // House
        Onset.getServer().addFunctionExport("SetHouseObjectBehavior", new SetHouseObjectBehaviorEF());

        // Player
        Onset.getServer().addFunctionExport("AddBankMoney", new AddBankMoneyEF());

        // Inv
        Onset.getServer().addFunctionExport("CreateChest", new CreateChestEF());
        Onset.getServer().addFunctionExport("OpenChest", new OpenChestEF());
        Onset.getServer().addFunctionExport("GetChestItems", new GetChestItemsEF());
        Onset.getServer().addFunctionExport("AddChestItem", new AddChestItemEF());
        Onset.getServer().addFunctionExport("RemoveChestItem", new RemoveChestItemEF());
        Onset.getServer().addFunctionExport("GetPlayerMainInventoryId", new GetPlayerMainInventoryIdEF());

        // WorldUI
        Onset.getServer().addFunctionExport("CreateWUI", new CreateWorldUIEF());
        Onset.getServer().addFunctionExport("DestroyWUI", new DestroyWUIEF());
        Onset.getServer().addFunctionExport("SetProgressWUI", new SetProgressWUIEF());
        Onset.getServer().addFunctionExport("SetImageWUI", new SetImageWUIEF());
    }
}
