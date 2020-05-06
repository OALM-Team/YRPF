package fr.yuki.YukiRPFramework.character;

import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import net.onfirenetwork.onsetjava.entity.Player;

public class CharacterState {
    private WearableWorldObject wearableWorldObject;

    public CharacterState() {}

    public WearableWorldObject getWearableWorldObject() {
        return wearableWorldObject;
    }

    public void setWearableWorldObject(Player player, WearableWorldObject wearableWorldObject) {
        this.wearableWorldObject = wearableWorldObject;
        if(this.wearableWorldObject != null) {
            player.setProperty("wearId", this.wearableWorldObject.getUuid(), true);
        } else {
            player.setProperty("wearId", "", true);
        }
    }
}
