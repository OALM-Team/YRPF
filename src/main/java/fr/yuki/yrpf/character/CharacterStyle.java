package fr.yuki.yrpf.character;

import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

public class CharacterStyle {
    private String gender;
    private String body;
    private String hair;
    private String hairColor;
    private String top;
    private String pant;
    private String shoes;

    public void attachStyleToPlayer(Player player) {
        Onset.print("Attach player style to character");
        player.setProperty("gender", this.gender, true);
        player.setProperty("body", this.body, true);
        player.setProperty("hair", this.hair, true);
        player.setProperty("hairColor", this.hairColor.substring(1), true);
        player.setProperty("top", this.top, true);
        player.setProperty("pant", this.pant, true);
        player.setProperty("shoes", this.shoes, true);

        for(Player otherPlayer : Onset.getPlayers()) {
            otherPlayer.callRemoteEvent("Character:Style:SetPart", player.getId(), "gender", this.gender);
            otherPlayer.callRemoteEvent("Character:Style:SetPart", player.getId(), "body", this.body);
            otherPlayer.callRemoteEvent("Character:Style:SetPart", player.getId(), "hair", this.hair);
            otherPlayer.callRemoteEvent("Character:Style:SetPart", player.getId(), "hairColor", this.hairColor.substring(1));
            otherPlayer.callRemoteEvent("Character:Style:SetPart", player.getId(), "top", this.top);
            otherPlayer.callRemoteEvent("Character:Style:SetPart", player.getId(), "pant", this.pant);
            otherPlayer.callRemoteEvent("Character:Style:SetPart", player.getId(), "shoes", this.shoes);
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getPant() {
        return pant;
    }

    public void setPant(String pant) {
        this.pant = pant;
    }

    public String getShoes() {
        return shoes;
    }

    public void setShoes(String shoes) {
        this.shoes = shoes;
    }
}
