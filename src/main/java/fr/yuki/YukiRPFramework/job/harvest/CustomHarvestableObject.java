package fr.yuki.YukiRPFramework.job.harvest;

import fr.yuki.YukiRPFramework.character.CharacterLoopAnimation;
import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.job.WorldHarvestObject;
import fr.yuki.YukiRPFramework.manager.InventoryManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;
import net.onfirenetwork.onsetjava.plugin.event.Event;

import java.util.ArrayList;

public class CustomHarvestableObject implements HarvestableObject {
    private final String name;
    private final int xp;
    private final int baseHarvestTime;
    private final int levelRequired;
    private final int modelId;
    private final int distanceToInteract;
    private CharacterLoopAnimation characterLoopAnimation = null;

    private int requiredItem = -1;

    public CustomHarvestableObject(String name, int xp, int baseHarvestTime,
                                   int levelRequired, int modelId, int distanceToInteract) {
        this.name = name;
        this.xp = xp;
        this.baseHarvestTime = baseHarvestTime;
        this.levelRequired = levelRequired;
        this.modelId = modelId;
        this.distanceToInteract = distanceToInteract;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getXp() {
        return this.xp;
    }

    @Override
    public ArrayList<Vector> getSpawns() {
        return null;
    }

    @Override
    public int getBaseHarvestTime() {
        return this.baseHarvestTime;
    }

    @Override
    public int getLevelRequired() {
        return this.levelRequired;
    }

    @Override
    public boolean needItemToHarvest() {
        return false;
    }

    @Override
    public int getModelId() {
        return this.modelId;
    }

    @Override
    public Vector getScale() {
        return new Vector(1, 1, 1);
    }

    @Override
    public ItemTemplateEnum getItemRequired() {
        return null;
    }

    @Override
    public int distanceToInteract() {
        return this.distanceToInteract;
    }

    @Override
    public boolean checkRequirements(Player player) {
        if(this.requiredItem != -1) {
            Inventory inventory = InventoryManager.getMainInventory(player);
            if(inventory.getItemByType(String.valueOf(this.requiredItem)) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public CharacterLoopAnimation getCharacterLoopHarvestAnimation(Player player) {
        CharacterLoopAnimation newLoop = new CharacterLoopAnimation(player, this.characterLoopAnimation.getAnimation(),
                this.characterLoopAnimation.getLoopInterval(), this.characterLoopAnimation.getLoopAmount(),
                this.characterLoopAnimation.getLoopSound());
        return newLoop;
    }

    @Override
    public void onHarvestDone(Player player, WorldHarvestObject worldHarvestObject) {
        Onset.getServer().callLuaEvent("YRPF:JobAPI:OnHarvestDone", player.getId(),
                worldHarvestObject.getJob().getJobType(), worldHarvestObject.getHarvestableObject().getName(), worldHarvestObject.getUuid());
    }

    public int getRequiredItem() {
        return requiredItem;
    }

    public void setRequiredItem(int requiredItem) {
        this.requiredItem = requiredItem;
    }

    public CharacterLoopAnimation getCharacterLoopAnimation() {
        return characterLoopAnimation;
    }

    public void setCharacterLoopAnimation(CharacterLoopAnimation characterLoopAnimation) {
        this.characterLoopAnimation = characterLoopAnimation;
    }
}
