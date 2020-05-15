package fr.yuki.YukiRPFramework.job.placementObject;

import fr.yuki.YukiRPFramework.job.ObjectPlacementInstance;
import fr.yuki.YukiRPFramework.job.deliveryPackage.GrowBoxDeliveryPackage;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class GrowBoxPlacementInstance extends ObjectPlacementInstance {
    public GrowBoxPlacementInstance(int modelId, Vector spawnPoint) {
        super(modelId, spawnPoint);
    }

    @Override
    public void onPlacementDone(Player player, Vector position, Vector rotation) {
        GrowBoxDeliveryPackage growBoxDeliveryPackage = new GrowBoxDeliveryPackage(player, position, rotation);
        growBoxDeliveryPackage.spawn();
    }
}
