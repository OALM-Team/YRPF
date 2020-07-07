package fr.yuki.YukiRPFramework.job.harvest;

import fr.yuki.YukiRPFramework.character.CharacterLoopAnimation;
import fr.yuki.YukiRPFramework.character.CharacterToolAnimation;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.job.WorldHarvestObject;
import fr.yuki.YukiRPFramework.manager.InventoryManager;
import fr.yuki.YukiRPFramework.manager.JobManager;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.ArrayList;

public class IronOre implements HarvestableObject {
    @Override
    public String getName() {
        return "Fer Brute";
    }

    @Override
    public int getXp() {
        return 20;
    }

    @Override
    public ArrayList<Vector> getSpawns() {
        return new ArrayList<>();
    }

    @Override
    public int getBaseHarvestTime() {
        return 25000;
    }

    @Override
    public int getLevelRequired() {
        return 2;
    }

    @Override
    public boolean needItemToHarvest() {
        return true;
    }

    @Override
    public int getModelId() {
        return 154;
    }

    @Override
    public Vector getScale() {
        return new Vector(0.1, 0.1, 0.1);
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
                new CharacterToolAnimation(154, new Vector(0,15,0), new Vector(90,-90,0), new Vector(0.09,0.09,0.09), "hand_r"),
                new Vector(worldHarvestObject.getJobSpawnPosition().getX(), worldHarvestObject.getJobSpawnPosition().getY(),
                        worldHarvestObject.getJobSpawnPosition().getZ()));
        JobManager.getWearableWorldObjects().add(wearableWorldObject);
    }
}
