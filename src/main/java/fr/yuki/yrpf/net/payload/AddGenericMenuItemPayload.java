package fr.yuki.yrpf.net.payload;

public class AddGenericMenuItemPayload {
    private String type;
    private String text;
    private String action;

    public AddGenericMenuItemPayload(String text, String action) {
        this.type = "ADD_GENERIC_MENU_ITEM";
        this.text = text;
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getAction() {
        return action;
    }
}
