package fr.yuki.YukiRPFramework.job.harvest;

import fr.yuki.YukiRPFramework.character.CharacterLoopAnimation;
import fr.yuki.YukiRPFramework.character.CharacterToolAnimation;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.job.WorldHarvestObject;
import fr.yuki.YukiRPFramework.manager.InventoryManager;
import fr.yuki.YukiRPFramework.utils.Basic;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.ArrayList;

public class Salmon implements HarvestableObject {
    @Override
    public String getName() {
        return "Saumon";
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
        return 15000;
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
        return 50004;
    }

    @Override
    public ItemTemplateEnum getItemRequired() {
        return ItemTemplateEnum.FISHING_ROD;
    }

    @Override
    public int distanceToInteract() {
        return 2500;
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
        CharacterLoopAnimation characterLoopAnimation = new CharacterLoopAnimation(player, Animation.FISHING, 5000, 1,
                "sounds/fishing_1.mp3");
        characterLoopAnimation.setTool(new CharacterToolAnimation(1111, new Vector(-10,10,-20),
                new Vector(0,0,0), new Vector(1, 1, 1), "hand_r"));
        return characterLoopAnimation;
    }

    @Override
    public void onHarvestDone(Player player, WorldHarvestObject worldHarvestObject) {
        InventoryManager.addItemToPlayer(player, ItemTemplateEnum.SALMON.id, Basic.randomNumber(1, 4), true);
    }

    @Override
    public Vector getScale() {
        return new Vector(1, 1, 1);
    }
}
