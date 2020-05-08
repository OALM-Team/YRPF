package fr.yuki.YukiRPFramework.net.payload;

public class AddXpBarItemPayload {
    private String type;
    private String text;

    public AddXpBarItemPayload(String text) {
        this.text = text;
        this.type = "ADD_XPBAR_ITEM";
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
