package fr.yuki.yrpf.job.harvest;

import fr.yuki.yrpf.character.CharacterLoopAnimation;
import fr.yuki.yrpf.character.CharacterToolAnimation;
import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.job.WearableWorldObject;
import fr.yuki.yrpf.job.WorldHarvestObject;
import fr.yuki.yrpf.manager.InventoryManager;
import fr.yuki.yrpf.manager.JobManager;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.ArrayList;

public class CooperOre implements HarvestableObject {
    @Override
    public String getName() {
        return "Mineraie de cuivre";
    }

    @Override
    public int getXp() {
        return 15;
    }

    @Override
    public ArrayList<Vector> getSpawns() {
        return new ArrayList<>();
    }

    @Override
    public int getBaseHarvestTime() {
        return 20000;
    }

    @Override
    public int getLevelRequired() {
        return 0;
    }

    @Override
    public boolean needItemToHarvest() {
        return true;
    }

    @Override
    public int getModelId() {
        return 156;
    }

    @Override
    public ItemTemplateEnum getItemRequired() {
        return ItemTemplateEnum.MINER_PICKAXE_1;
    }

    @Override
    public int distanceToInteract() {
        return 300;
    }

    @Override
    public boolean checkRequirements(Player player) {
        Inventory inventory = InventoryManager.getMainInventory(player);
        if(inventory.getItemByType(this.getItemRequired().id) == null) {
            return false;
        }
        return true;
    }

    @Override
    public Vector getScale() {
        return new Vector(1, 1, 1);
    }

    @Override
    public CharacterLoopAnimation getCharacterLoopHarvestAnimation(Player player) {
        CharacterLoopAnimation characterLoopAnimation = new CharacterLoopAnimation(player, Animation.PICKAXE_SWING, 5000, 4,
                "sounds/pickaxe_hit.mp3");
        characterLoopAnimation.setTool(new CharacterToolAnimation(50003, new Vector(-8,5,20),
                new Vector(-90,90,0), new Vector(2, 2, 2), "hand_r"));
        return characterLoopAnimation;
    }

    @Override
    public void onHarvestDone(Player player, WorldHarvestObject worldHarvestObject) {
        WearableWorldObject wearableWorldObject = new WearableWorldObject(156, true, Animation.CARRY_IDLE,
                new CharacterToolAnimation(156, new Vector(0,15,0), new Vector(90,-90,0), new Vector(0.55,0.55,0.55), "hand_r"),
                new Vector(worldHarvestObject.getJobSpawnPosition().getX(), worldHarvestObject.getJobSpawnPosition().getY(),
                        worldHarvestObject.getJobSpawnPosition().getZ()));
        JobManager.getWearableWorldObjects().add(wearableWorldObject);
    }
}
