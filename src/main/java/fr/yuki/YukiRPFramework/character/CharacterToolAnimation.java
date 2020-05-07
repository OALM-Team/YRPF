package fr.yuki.YukiRPFramework.character;

import fr.yuki.YukiRPFramework.manager.ModdingManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;
import net.onfirenetwork.onsetjava.enums.AttachType;

public class CharacterToolAnimation {
    private WorldObject toolObject;
    private int modelId;
    private Vector position;
    private Vector rotation;
    private Vector scale;
    private String attachSocket;

    public CharacterToolAnimation(int modelId, Vector position, Vector rotation, Vector scale, String attachSocket) {
        this.modelId = modelId;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.attachSocket = attachSocket;
    }

    public void attach(Player player) {
        this.toolObject = Onset.getServer().createObject(new Vector(0, 0, 0), this.modelId);
        if(ModdingManager.isCustomModelId(this.modelId)) {
            ModdingManager.assignCustomModel(this.toolObject, this.modelId);
        }
        this.toolObject.attach(player, this.position, this.rotation, this.attachSocket);
        this.toolObject.setScale(this.scale);
    }

    public void unAttach() {
        this.toolObject.destroy();
    }

    public Vector getScale() {
        return scale;
    }
}
