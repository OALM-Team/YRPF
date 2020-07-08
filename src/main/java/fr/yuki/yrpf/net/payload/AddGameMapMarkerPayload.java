package fr.yuki.yrpf.net.payload;

import fr.yuki.yrpf.model.GameMapMarker;

public class AddGameMapMarkerPayload {
    private String type;
    private GameMapMarker marker;

    public AddGameMapMarkerPayload(GameMapMarker marker) {
        this.marker = marker;
        this.type = "GAMEMAP_ADD_MARKER";
    }

    public String getType() {
        return type;
    }

    public GameMapMarker getMarker() {
        return marker;
    }
}
