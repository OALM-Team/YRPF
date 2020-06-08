package fr.yuki.YukiRPFramework.tebex.responses;

public class PlayerMeta {
    private String avatar;
    private String avatarfull;
    private String steamID;

    public String getAvatar() { return avatar; }
    public void setAvatar(String value) { this.avatar = value; }

    public String getAvatarfull() { return avatarfull; }
    public void setAvatarfull(String value) { this.avatarfull = value; }

    public String getSteamID() { return steamID; }
    public void setSteamID(String value) { this.steamID = value; }
}