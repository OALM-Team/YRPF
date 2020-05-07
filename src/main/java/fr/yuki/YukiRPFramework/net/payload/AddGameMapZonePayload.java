package fr.yuki.YukiRPFramework.net.payload;

import fr.yuki.YukiRPFramework.model.GameMapMarker;
import fr.yuki.YukiRPFramework.model.GameMapZone;

public class AddGameMapZonePayload {
    private String type;
    private GameMapZone zone;

    public AddGameMapZonePayload(GameMapZone zone) {
        this.zone = zone;
        this.type = "GAMEMAP_ADD_ZONE";
    }

    public String getType() {
        return type;
    }

    public GameMapZone getZone() {
        return zone;
    }
}
