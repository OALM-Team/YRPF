package fr.yuki.yrpf.world;

import com.google.gson.Gson;
import fr.yuki.yrpf.net.payload.AddGameMapMarkerPayload;
import lombok.Getter;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;

import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class WorldUI {
    private static int WORLD_UI_ID = 1;
    private final int width;
    private final int height;

    @Getter
    private int id;
    @Getter
    private Vector position;
    @Getter
    private Vector rotation;
    @Getter
    private String uiType;
    @Getter
    private WorldObject worldObject;
    @Getter
    private double streamDistance;


    public WorldUI(Vector position, Vector rotation, int width, int height, String uiType) {
        this.id = WORLD_UI_ID++;
        this.position = position;
        this.rotation = rotation;
        this.width = width;
        this.height = height;
        this.uiType = uiType;
        this.streamDistance = 5000;
        this.spawn();
    }

    public abstract void sync();

    private void spawn() {
        this.worldObject = Onset.getServer().createObject(this.position, 1);
        this.worldObject.setProperty("isWorldUI", "true", true);
        this.worldObject.setProperty("uiID", this.id, true);
        this.worldObject.setProperty("uiType", this.uiType, true);
        this.worldObject.setProperty("uiWidth", this.width, true);
        this.worldObject.setProperty("uiHeight", this.height, true);
        this.worldObject.setStreamDistance(this.streamDistance);
    }

    public void destroy() {
        if(this.worldObject != null) {
            this.worldObject.destroy();
            this.worldObject = null;
        }
    }

    protected ArrayList<Player> getPlayersInRange() {
        return new ArrayList<>(Onset.getPlayers().stream().filter(x -> x.getLocation().distance(this.position) <= this.streamDistance)
                .collect(Collectors.toList()));
    }

    public void dispatchToPlayerUI(Player player, String payload) {
        player.callRemoteEvent("GlobalUI:DispatchToWUI", this.id, payload);
    }
}
