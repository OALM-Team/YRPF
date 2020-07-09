package fr.yuki.yrpf.manager;

import fr.yuki.yrpf.character.CharacterState;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Door;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

public class PoliceManager {
    public static void handleKickDoor(Player player) {
        Door door = WorldManager.getNearestDoor(player.getLocation());
        if(door.getLocation().distance(player.getLocation()) > 200) return;
        if(door.isOpen()) return;
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        if(characterState.getCurrentGenericMenu() != null) {
            characterState.getCurrentGenericMenu().hide();
            characterState.setCurrentGenericMenu(null);
        }
        player.setAnimation(Animation.KICKDOOR);
        Onset.delay(300, () -> {
            SoundManager.playSound3D("sounds/door_kick.mp3", player.getLocation(), 1000, 0.5);
            door.setOpen(true);
        });
    }
}
