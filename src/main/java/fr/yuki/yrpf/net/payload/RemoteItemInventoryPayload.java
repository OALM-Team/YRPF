package fr.yuki.yrpf.net.payload;

public class RemoteItemInventoryPayload {
    private String type;
    private String id;

    public RemoteItemInventoryPayload(String id) {
        this.id = id;
        this.type = "REMOVE_ITEM_INVENTORY";

    }

    public String getId() {
        return id;
    }
}
