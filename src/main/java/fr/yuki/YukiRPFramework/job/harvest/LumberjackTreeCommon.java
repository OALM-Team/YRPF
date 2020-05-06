package fr.yuki.YukiRPFramework.job.harvest;

import fr.yuki.YukiRPFramework.character.CharacterLoopAnimation;
import fr.yuki.YukiRPFramework.character.CharacterToolAnimation;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import fr.yuki.YukiRPFramework.job.WorldHarvestObject;
import fr.yuki.YukiRPFramework.manager.InventoryManager;
import fr.yuki.YukiRPFramework.manager.JobManager;
import fr.yuki.YukiRPFramework.manager.ModdingManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.ArrayList;

public class LumberjackTreeCommon implements HarvestableObject {
    @Override
    public String getName() {
        return "Arbre commun";
    }

    @Override
    public ArrayList<Vector> getSpawns() {
        return new ArrayList<>();
    }

    @Override
    public int getBaseHarvestTime() {
        return 3000;
        //return 20000;
    }

    @Override
    public int getLevelRequired() {
        return 1;
    }

    @Override
    public boolean needItemToHarvest() {
        return false;
    }

    @Override
    public int getModelId() {
        return 143;
    }

    @Override
    public ItemTemplateEnum getItemRequired() {
        return ItemTemplateEnum.LUMBERJACK_HATCHET_1;
    }

    @Override
    public int distanceToInteract() {
        return 200;
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
        CharacterLoopAnimation characterLoopAnimation = new CharacterLoopAnimation(player, Animation.PICKAXE_SWING, 4000, 5,
                "sounds/chop_wood.mp3");
        characterLoopAnimation.setTool(new CharacterToolAnimation(50000, new Vector(-45,-6,20),
                new Vector(0,0,0), new Vector(0.5, 0.5, 0.5), "hand_r"));
        return characterLoopAnimation;
    }

    @Override
    public void onHarvestDone(Player player, WorldHarvestObject worldHarvestObject) {
        WearableWorldObject wearableWorldObject = new WearableWorldObject(50001, true, Animation.CARRY_SHOULDER_IDLE,
                new CharacterToolAnimation(50001, new Vector(5,8,0), new Vector(0,0,90), new Vector(0.5,0.5,0.5), "hand_r"),
                new Vector(worldHarvestObject.getJobSpawnPosition().getX(), worldHarvestObject.getJobSpawnPosition().getY(),
                    worldHarvestObject.getJobSpawnPosition().getZ()));
        JobManager.getWearableWorldObjects().add(wearableWorldObject);
    }
}
