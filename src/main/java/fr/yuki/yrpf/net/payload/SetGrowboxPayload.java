package fr.yuki.yrpf.net.payload;

public class SetGrowboxPayload {
    private String type;
    private String id;

    public SetGrowboxPayload(String id) {
        this.id = id;
        this.type = "SET_GROWBOX";
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
