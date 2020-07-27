package fr.yuki.yrpf.net.payload;

public class RemoveMapMarkerPayload {
    private String type;
    private int id;

    public RemoveMapMarkerPayload(int id) {
        this.id = id;
        this.type = "GAMEMAP_REMOVE_MARKER";
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
