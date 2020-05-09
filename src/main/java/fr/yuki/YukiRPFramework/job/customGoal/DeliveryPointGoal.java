package fr.yuki.YukiRPFramework.job.customGoal;

import fr.yuki.YukiRPFramework.job.DeliveryPointConfig;
import fr.yuki.YukiRPFramework.job.JobSpawnPosition;
import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class DeliveryPointGoal {
    private JobSpawnPosition position;
    private WearableWorldObject wearableWorldObject;

    public DeliveryPointGoal(JobSpawnPosition position, WearableWorldObject wearableWorldObject) {
        this.position = position;
        this.wearableWorldObject = wearableWorldObject;
    }

    public JobSpawnPosition getPosition() {
        return position;
    }

    public WearableWorldObject getWearableWorldObject() {
        return wearableWorldObject;
    }

    public boolean isNear(Player player) {
        return player.getLocation().distance(new Vector(this.position.getX(), this.position.getY(), this.position.getZ())) <= 200;
    }
}
