package fr.yuki.yrpf.net.payload;

public class SetGenericMenuImagePayload {
    private String imageUrl;
    private String type;

    public SetGenericMenuImagePayload(String imageUrl) {
        this.type = "SET_GENERIC_MENU_IMAGE";
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
