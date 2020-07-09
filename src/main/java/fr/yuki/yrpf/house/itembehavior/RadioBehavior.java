package fr.yuki.yrpf.house.itembehavior;

import fr.yuki.yrpf.manager.SoundManager;
import fr.yuki.yrpf.model.HouseItemObject;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;

import java.util.UUID;

public class RadioBehavior extends ItemBehavior {
    private String soundId;
    private WorldObject soundObject;

    public RadioBehavior(HouseItemObject houseItemObject) {
        super(houseItemObject);
        this.soundId = "radio-" + UUID.randomUUID().toString();
    }

    @Override
    public void onSpawn() {
        String songName = "";
        switch (houseItemObject.getModelId()) {
            case 1194:
                songName = "sounds/shop_ambiant2.mp3";
                break;

            default:
                songName = "sounds/shop_ambiant.mp3";
                break;
        }
        this.soundObject = SoundManager.createAmbiantSound(this.soundId, songName,
                houseItemObject.getPosition(), 800, 0.3);
    }

    @Override
    public void onDestroy() {
        this.soundObject.destroy();
        this.soundObject = null;
        SoundManager.getAmbiantSounds().remove(this.soundId);
    }

    @Override
    public void onInteract(Player player) {

    }
}
