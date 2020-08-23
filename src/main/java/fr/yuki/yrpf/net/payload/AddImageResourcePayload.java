package fr.yuki.yrpf.net.payload;

public class AddImageResourcePayload {
    private String group;
    private String key;
    private String value;
    private String type;

    public AddImageResourcePayload(String group, String key, String value) {
        this.group = group;
        this.key = key;
        this.value = value;
        this.type = "ADD_IMAGE_RESOURCE";
    }

    public String getGroup() {
        return group;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
