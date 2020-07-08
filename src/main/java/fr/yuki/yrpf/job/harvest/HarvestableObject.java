package fr.yuki.yrpf.job.harvest;

import fr.yuki.yrpf.character.CharacterLoopAnimation;
import fr.yuki.yrpf.enums.ItemTemplateEnum;
import fr.yuki.yrpf.job.WorldHarvestObject;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;

public interface HarvestableObject {
    String getName();
    int getXp();
    ArrayList<Vector> getSpawns();
    int getBaseHarvestTime();
    int getLevelRequired();
    boolean needItemToHarvest();
    int getModelId();
    Vector getScale();
    ItemTemplateEnum getItemRequired();
    int distanceToInteract();
    boolean checkRequirements(Player player);
    CharacterLoopAnimation getCharacterLoopHarvestAnimation(Player player);
    void onHarvestDone(Player player, WorldHarvestObject worldHarvestObject);
}
