package fr.yuki.yrpf.net.payload;

public class AddCustomItemImagePayload {
    private int id;
    private String path;
    private String type;

    public AddCustomItemImagePayload(int id, String path) {
        this.id = id;
        this.path = path;
        this.type = "ADD_CUSTOM_ITEM_IMAGE";
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }
}
