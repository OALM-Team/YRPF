package fr.yuki.YukiRPFramework.job.tools;

import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import net.onfirenetwork.onsetjava.entity.Player;

public interface JobToolHandler {
    boolean canInteract(Player player);
    boolean onUnwear(Player player, WearableWorldObject wearableWorldObject);
    boolean hasLevelRequired(Player player);
}
