package fr.yuki.YukiRPFramework.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.yuki.YukiRPFramework.character.CharacterJobLevel;
import fr.yuki.YukiRPFramework.character.CharacterStyle;
import fr.yuki.YukiRPFramework.net.payload.RequestBuyVehiclePayload;

import java.util.ArrayList;
import java.util.Date;

public class Account {
    private int id;
    private int adminLevel;
    private String steamName;
    private String steamId;
    private Date createdAt;
    private Date updatedAt;
    private int isBanned;
    private int bankMoney;
    private double saveX;
    private double saveY;
    private double saveZ;
    private double saveH;
    private int characterCreationRequest;
    private String characterStyle;
    private String characterName;
    private String jobLevels;
    private int isDead;
    private String lang;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSteamName() {
        return steamName;
    }

    public void setSteamName(String steamName) {
        this.steamName = steamName;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(int isBanned) {
        this.isBanned = isBanned;
    }

    public int getBankMoney() {
        return bankMoney;
    }

    public void setBankMoney(int bankMoney) {
        this.bankMoney = bankMoney;
    }

    public double getSaveX() {
        return saveX;
    }

    public void setSaveX(double saveX) {
        this.saveX = saveX;
    }

    public double getSaveY() {
        return saveY;
    }

    public void setSaveY(double saveY) {
        this.saveY = saveY;
    }

    public double getSaveZ() {
        return saveZ;
    }

    public void setSaveZ(double saveZ) {
        this.saveZ = saveZ;
    }

    public double getSaveH() {
        return saveH;
    }

    public void setSaveH(double saveH) {
        this.saveH = saveH;
    }

    public int getCharacterCreationRequest() {
        return characterCreationRequest;
    }

    public void setCharacterCreationRequest(int characterCreationRequest) {
        this.characterCreationRequest = characterCreationRequest;
    }

    public String getCharacterStyle() {
        return characterStyle;
    }

    public void setCharacterStyle(String characterStyle) {
        this.characterStyle = characterStyle;
    }

    public void setCharacterStyle(CharacterStyle characterStyle) {
        this.characterStyle = new Gson().toJson(characterStyle);
    }

    public CharacterStyle decodeCharacterStyle() {
        if(this.characterStyle.equals("")) return new CharacterStyle();
        return new Gson().fromJson(this.characterStyle, CharacterStyle.class);
    }

    public void setJobLevels(ArrayList<CharacterJobLevel> jobLevels) {
        this.jobLevels = new Gson().toJson(jobLevels);
    }

    public ArrayList<CharacterJobLevel> decodeCharacterJob() {
        if(this.jobLevels.equals("")) return new ArrayList<>();
        return new Gson().fromJson(this.jobLevels, new TypeToken<ArrayList<CharacterJobLevel>>(){}.getType());
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getJobLevels() {
        return jobLevels;
    }

    public void setJobLevels(String jobLevels) {
        this.jobLevels = jobLevels;
    }

    public int getIsDead() {
        return isDead;
    }

    public void setIsDead(int isDead) {
        this.isDead = isDead;
    }

    public int getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(int adminLevel) {
        this.adminLevel = adminLevel;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
