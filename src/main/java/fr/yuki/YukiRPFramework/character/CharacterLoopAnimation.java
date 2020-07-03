package fr.yuki.YukiRPFramework.character;

import fr.yuki.YukiRPFramework.manager.SoundManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

public class CharacterLoopAnimation {
    private Player player;
    private String animation;
    private int loopInterval;
    private int loopAmount;
    private String loopSound;
    private boolean isActive = false;
    private CharacterToolAnimation characterToolAnimation;

    public CharacterLoopAnimation(String animation, int loopInterval, int loopAmount,
                                  String loopSound) {
        this.animation = animation;
        this.loopInterval = loopInterval;
        this.loopAmount = loopAmount;
        this.loopSound = loopSound;
    }

    public CharacterLoopAnimation(Player player, String animation, int loopInterval, int loopAmount,
                                  String loopSound) {
        this.player = player;
        this.animation = animation.toString();
        this.loopInterval = loopInterval;
        this.loopAmount = loopAmount;
        this.loopSound = loopSound;
    }

    public CharacterLoopAnimation(Player player, Animation animation, int loopInterval, int loopAmount,
                                  String loopSound) {
        this.player = player;
        this.animation = animation.toString();
        this.loopInterval = loopInterval;
        this.loopAmount = loopAmount;
        this.loopSound = loopSound;
    }

    public void start() {
        this.isActive = true;
        if(!loopSound.equals("")) {
            SoundManager.playSound3D(this.loopSound, this.player.getLocation(), 1500, 1);
        }

        // Attach the tool
        if(characterToolAnimation != null) {
            this.attachTool();
        }

        this.player.setAnimation(this.animation);
        for(int i = 1; i <= loopAmount; i++) {
            int currentI = i;
            Onset.delay(loopInterval * i, () -> {
                if (!this.isActive) return;
                this.player.setAnimation(this.animation);

                if(!loopSound.equals("") && currentI != loopAmount) {
                    SoundManager.playSound3D(this.loopSound, this.player.getLocation(), 1500, 1);
                }
            });
        }
    }

    private void attachTool() {
        this.characterToolAnimation.attach(this.player);
    }

    public void setTool(CharacterToolAnimation characterToolAnimation) {
        this.characterToolAnimation = characterToolAnimation;
    }

    public void stop() {
        this.isActive = false;
        if(characterToolAnimation != null) {
            this.characterToolAnimation.unAttach();
        }
        this.player.setAnimation(Animation.STOP);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public int getLoopInterval() {
        return loopInterval;
    }

    public void setLoopInterval(int loopInterval) {
        this.loopInterval = loopInterval;
    }

    public int getLoopAmount() {
        return loopAmount;
    }

    public void setLoopAmount(int loopAmount) {
        this.loopAmount = loopAmount;
    }

    public String getLoopSound() {
        return loopSound;
    }

    public void setLoopSound(String loopSound) {
        this.loopSound = loopSound;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public CharacterToolAnimation getCharacterToolAnimation() {
        return characterToolAnimation;
    }

    public void setCharacterToolAnimation(CharacterToolAnimation characterToolAnimation) {
        this.characterToolAnimation = characterToolAnimation;
    }
}
