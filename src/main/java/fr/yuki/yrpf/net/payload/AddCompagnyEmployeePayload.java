package fr.yuki.yrpf.net.payload;

public class AddCompagnyEmployeePayload {
    private String type;
    private String steamid;
    private String name;
    private int rank;
    private boolean online;

    public AddCompagnyEmployeePayload(String steamid, String name, int rank, boolean online) {
        this.type = "ADD_COMPAGNY_EMPLOYEE";
        this.steamid = steamid;
        this.name = name;
        this.rank = rank;
        this.online = online;
    }

    public String getType() {
        return type;
    }

    public String getSteamid() {
        return steamid;
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }

    public boolean isOnline() {
        return online;
    }
}
