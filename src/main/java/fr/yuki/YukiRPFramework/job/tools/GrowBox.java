package fr.yuki.YukiRPFramework.job.tools;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.job.tools.growbox.Pot;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.InventoryManager;
import fr.yuki.YukiRPFramework.manager.UIStateManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.JobTool;
import fr.yuki.YukiRPFramework.net.payload.AddGrowboxMenuItemPayload;
import fr.yuki.YukiRPFramework.net.payload.AddToastPayload;
import fr.yuki.YukiRPFramework.net.payload.SetGrowboxPayload;
import fr.yuki.YukiRPFramework.vehicle.storeLayout.StoreLayoutTransform;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class GrowBox implements JobToolHandler {
    private JobTool jobTool;
    private ArrayList<Pot> pots;
    private ArrayList<StoreLayoutTransform> layoutTransforms;

    public GrowBox(JobTool jobTool) {
        this.jobTool = jobTool;
        this.pots = new ArrayList<>();

        this.layoutTransforms = new ArrayList<>();
        this.layoutTransforms.add(new StoreLayoutTransform(0, new Vector(0, 0, 0), new Vector(0, 0, 0), new Vector(1.50, 1.50, 2.40)));
        this.layoutTransforms.add(new StoreLayoutTransform(1, new Vector(50, 0, 0), new Vector(0, 0, 0), new Vector(1.50, 1.50, 2.40)));
        this.layoutTransforms.add(new StoreLayoutTransform(2, new Vector(0, 50, 0), new Vector(0, 0, 0), new Vector(1.50, 1.50, 2.40)));
        this.layoutTransforms.add(new StoreLayoutTransform(3, new Vector(50, 50, 0), new Vector(0, 0, 0), new Vector(1.50, 1.50, 2.40)));
        this.layoutTransforms.add(new StoreLayoutTransform(3, new Vector(0, -50, 0), new Vector(0, 0, 0), new Vector(1.50, 1.50, 2.40)));
        this.layoutTransforms.add(new StoreLayoutTransform(3, new Vector(50, -50, 0), new Vector(0, 0, 0), new Vector(1.50, 1.50, 2.40)));
    }

    private void resyncPots() {
        for(int i = 0; i < this.pots.size(); i++) {
            this.pots.get(i).destroyWorldObject();
            this.pots.get(i).spawnWorldObject(this.layoutTransforms.get(i));
        }
    }

    public int getMaxPots() {
        return 6;
    }

    @Override
    public boolean canInteract(Player player) {
        CharacterState state = CharacterManager.getCharacterStateByPlayer(player);
        if(state.getWearableWorldObject() == null) return false;
        if(state.getWearableWorldObject().getModelId() != 554) return false;
        return true;
    }

    @Override
    public boolean onUnwear(Player player, WearableWorldObject wearableWorldObject) {
        switch (wearableWorldObject.getModelId()) {
            case 554: // Pot
                return this.addPot(player, wearableWorldObject);
        }
        return false;
    }

    @Override
    public boolean hasLevelRequired(Player player) {
        return true;
    }

    @Override
    public boolean onUse(Player player) {
        if(!UIStateManager.handleUIToogle(player, "growboxmenu")) {
            CharacterManager.setCharacterFreeze(player, false);
            return true;
        }
        CharacterManager.setCharacterFreeze(player, true);
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetGrowboxPayload(this.jobTool.getUuid())));
        for(Pot pot : this.pots) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddGrowboxMenuItemPayload(pot.getUuid(),
                    554, pot.getWater(), pot.getState())));
        }

        return true;
    }

    @Override
    public boolean canBeUse() {
        return true;
    }

    public boolean addPot(Player player, WearableWorldObject wearableWorldObject) {
        Onset.print("Add pot request for the growbox");
        Account account = WorldManager.getPlayerAccount(player);
        if(this.pots.size() + 1 > this.getMaxPots()) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.vehicle.no_space_left"));
            return false;
        }

        Pot pot = new Pot(this);
        this.pots.add(pot);

        this.resyncPots();
        return true;
    }

    public void fillPotWater(Player player, String potId) {
        Account account = WorldManager.getPlayerAccount(player);
        Pot pot = this.pots.stream().filter(x -> x.getUuid().equals(potId)).findFirst().orElse(null);
        if(pot == null) {
            return;
        }
        Inventory inventory = InventoryManager.getMainInventory(player);
        InventoryItem waterItem = inventory.getItemByType(ItemTemplateEnum.WATER.id);
        if(waterItem == null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.growbox.no_water"));
            return;
        }
        if(pot.getWater() == 100) return;
        pot.setWater(100);
        inventory.removeItem(waterItem, 1);
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddGrowboxMenuItemPayload(pot.getUuid(),
                554, pot.getWater(), pot.getState())));
    }

    public JobTool getJobTool() {
        return jobTool;
    }
}
