package fr.yuki.yrpf.job.tools;

import fr.yuki.yrpf.job.WearableWorldObject;
import net.onfirenetwork.onsetjava.entity.Player;

public interface JobToolHandler {
    boolean canInteract(Player player);
    boolean onUnwear(Player player, WearableWorldObject wearableWorldObject);
    boolean hasLevelRequired(Player player);
    boolean onUse(Player player);
    boolean canBeUse();
}
