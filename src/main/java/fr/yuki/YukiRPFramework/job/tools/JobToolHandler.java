package fr.yuki.YukiRPFramework.job.tools;

import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import net.onfirenetwork.onsetjava.entity.Player;

public interface JobToolHandler {
    boolean canInteract(Player player);
    void onUnwear(Player player, WearableWorldObject wearableWorldObject);
}
