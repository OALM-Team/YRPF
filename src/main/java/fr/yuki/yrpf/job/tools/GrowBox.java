package fr.yuki.yrpf.job.tools;

import com.google.gson.Gson;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.enums.JobEnum;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.inventory.InventoryItem;
import fr.yuki.yrpf.job.WearableWorldObject;
import fr.yuki.yrpf.job.tools.growbox.Pot;
import fr.yuki.yrpf.manager.*;
import fr.yuki.yrpf.modding.LoopSound3D;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.GrowboxModel;
import fr.yuki.yrpf.model.JobTool;
import fr.yuki.yrpf.net.payload.AddGrowboxMenuItemPayload;
import fr.yuki.yrpf.net.payload.SetGrowboxPayload;
import fr.yuki.yrpf.vehicle.storeLayout.StoreLayoutTransform;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;

public class GrowBox implements JobToolHandler {
    private JobTool jobTool;
    private ArrayList<Pot> pots;
    private ArrayList<StoreLayoutTransform> layoutTransforms;
    private LoopSound3D loopSound3D;
    private GrowboxModel growboxModel;

    public GrowBox(JobTool jobTool) {
        this.jobTool = jobTool;
        this.pots = new ArrayList<>();
        this.loopSound3D = new LoopSound3D("sounds/white_noise.mp3",
                new Vector(this.jobTool.getX(), this.jobTool.getY(), this.jobTool.getZ()), 1000, 0.10,
                1000*60);

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
        this.handlePotsRequest(player);
        return true;
    }

    public void handlePotsRequest(Player player) {
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetGrowboxPayload(this.jobTool.getUuid())));
        for(Pot pot : this.pots) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddGrowboxMenuItemPayload(pot.getUuid(),
                    554, pot.getWater(), pot.getState(), pot.isSeed())));
        }
    }

    @Override
    public boolean canBeUse() {
        return true;
    }

    public void destroy(Player player) {
        if(player != null) {
            UIStateManager.handleUIToogle(player, "growboxmenu");
            CharacterManager.setCharacterFreeze(player, false);
        }

        // Destroy pots
        for(Pot pot : this.pots) {
            pot.destroyWorldObject();
        }
        pots.clear();

        // Destroy growbox
        this.jobTool.destroy();
        this.growboxModel.delete();
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
                554, pot.getWater(), pot.getState(), pot.isSeed())));
    }

    public void fillPotSeed(Player player, String potId) {
        Account account = WorldManager.getPlayerAccount(player);
        Pot pot = this.pots.stream().filter(x -> x.getUuid().equals(potId)).findFirst().orElse(null);
        if(pot == null) {
            return;
        }
        Inventory inventory = InventoryManager.getMainInventory(player);
        InventoryItem seedItem = inventory.getItemByType(ItemTemplateEnum.WEED_SEED.id);
        if(seedItem == null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, I18n.t(account.getLang(), "toast.growbox.no_seed"));
            return;
        }
        if(pot.isSeed()) return;
        pot.setSeed(true);
        pot.spawnSeed();
        inventory.removeItem(seedItem, 1);
        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddGrowboxMenuItemPayload(pot.getUuid(),
                554, pot.getWater(), pot.getState(), pot.isSeed())));
        JobManager.addExp(player, JobEnum.WEED.name(), 15);
    }

    public void harvestPot(Player player, String potId) {
        Account account = WorldManager.getPlayerAccount(player);
        Pot pot = this.pots.stream().filter(x -> x.getUuid().equals(potId)).findFirst().orElse(null);
        if(pot == null) {
            return;
        }
        if(pot.getState() < 100) return;
        if(InventoryManager.addItemToPlayer(player, ItemTemplateEnum.WEED.id, 1, true) == null) {
            return;
        }
        pot.removeSeed();

        player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddGrowboxMenuItemPayload(pot.getUuid(),
                554, pot.getWater(), pot.getState(), pot.isSeed())));
    }

    public void takePot(Player player, String potId) {
        Account account = WorldManager.getPlayerAccount(player);
        Pot pot = this.pots.stream().filter(x -> x.getUuid().equals(potId)).findFirst().orElse(null);
        if(pot == null) {
            return;
        }
        if(InventoryManager.addItemToPlayer(player, ItemTemplateEnum.POT.id, 1, true) == null) {
            return;
        }
        pot.destroyWorldObject();
        this.pots.remove(pot);
        CharacterManager.setCharacterFreeze(player, false);
        UIStateManager.handleUIToogle(player, "growboxmenu");
    }

    public void tickGrow() {
        Generator generator = GrowboxManager.getGeneratorOnNearby(new Vector(this.jobTool.getX(), this.jobTool.getY(), this.jobTool.getZ()),
                1500);
        if(generator == null) {
            if(this.loopSound3D.isActive()) this.loopSound3D.stop();
            return;
        } else {
            if(!this.loopSound3D.isActive()) this.loopSound3D.start();
        }
        for(Pot pot : this.pots) {
            pot.grow();
        }
    }

    public JobTool getJobTool() {
        return jobTool;
    }

    public LoopSound3D getLoopSound3D() {
        return loopSound3D;
    }

    public GrowboxModel getGrowboxModel() {
        return growboxModel;
    }

    public void setGrowboxModel(GrowboxModel growboxModel) {
        this.growboxModel = growboxModel;
    }
}
