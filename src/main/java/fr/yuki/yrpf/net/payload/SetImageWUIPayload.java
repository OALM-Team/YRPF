package fr.yuki.yrpf.net.payload;

public class SetImageWUIPayload {
    private String imageUrl;
    private String type;

    public SetImageWUIPayload(String imageUrl) {
        this.type = "SET_IMAGE_WUI";
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }
}
