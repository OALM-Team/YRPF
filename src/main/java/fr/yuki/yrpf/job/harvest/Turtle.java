package fr.yuki.yrpf.job.harvest;

import fr.yuki.yrpf.character.CharacterLoopAnimation;
import fr.yuki.yrpf.character.CharacterToolAnimation;
import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.job.WorldHarvestObject;
import fr.yuki.yrpf.manager.InventoryManager;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.util.ArrayList;

public class Turtle implements HarvestableObject {
    @Override
    public String getName() {
        return "Tortue";
    }

    @Override
    public int getXp() {
        return 30;
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
        InventoryManager.addItemToPlayer(player, ItemTemplateEnum.TURTLE.id, 1, true);
    }

    @Override
    public Vector getScale() {
        return new Vector(1, 1, 1);
    }
}
