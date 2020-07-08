package fr.yuki.yrpf.tebex.responses;

public class TebexPlayer {
    private String id;
    private String name;
    private String uuid;
    private PlayerMeta meta;

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getUUID() { return uuid; }
    public void setUUID(String value) { this.uuid = value; }

    public PlayerMeta getMeta() { return meta; }
    public void setMeta(PlayerMeta value) { this.meta = value; }
}