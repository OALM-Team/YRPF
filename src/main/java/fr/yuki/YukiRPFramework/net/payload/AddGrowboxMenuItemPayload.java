package fr.yuki.YukiRPFramework.net.payload;

public class AddGrowboxMenuItemPayload {
    private String type;
    private String id;
    private int itemId;
    private int water;
    private int state;

    public AddGrowboxMenuItemPayload(String id, int itemId, int water, int state) {
        this.id = id;
        this.itemId = itemId;
        this.water = water;
        this.state = state;
        this.type = "ADD_GROWBOXMENU_ITEM";
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public int getWater() {
        return water;
    }

    public int getState() {
        return state;
    }
}
