package fr.yuki.yrpf.net.payload;

public class AddVChestItemPayload {
    private String type;
    private String uuid;
    private int modelId;
    private String name;

    public AddVChestItemPayload(String uuid, int modelId, String name) {
        this.uuid = uuid;
        this.modelId = modelId;
        this.name = name;
        this.type = "ADD_VCHEST_ITEM";
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public int getModelId() {
        return modelId;
    }

    public String getName() {
        return name;
    }
}
