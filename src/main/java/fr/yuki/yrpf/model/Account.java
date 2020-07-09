package fr.yuki.yrpf.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Dates;
import eu.bebendorf.ajorm.annotation.Table;
import fr.yuki.yrpf.character.CharacterJobLevel;
import fr.yuki.yrpf.character.CharacterStyle;
import fr.yuki.yrpf.manager.WeaponManager;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.entity.Player;

import java.sql.Timestamp;
import java.util.ArrayList;

@Dates @Getter @Setter @Table("tbl_account")
public class Account extends Model {
    @Column(column = "id_account")
    private int id;
    @Column
    private int adminLevel = 0;
    @Column(column = "steam_account_name")
    private String steamName;
    @Column
    private String steamId;
    @Column
    private boolean isBanned = false;
    @Column
    private int bankMoney = 4000;
    @Column
    private double saveX;
    @Column
    private double saveY;
    @Column
    private double saveZ;
    @Column
    private double saveH;
    @Column(column = "character_creation_request")
    private int characterCreationRequest = 1;
    @Column
    private String characterStyle = "";
    @Column
    private String characterName = "Unknown";
    @Column
    private String jobLevels = "[]";
    @Column
    private boolean isDead = false;
    @Column
    private String lang = "fr";
    @Column
    private Integer foodState = 100;
    @Column
    private Integer drinkState = 100;
    @Column(column = "phone_number")
    private String phoneNumber;
    @Column
    private String weapons = "[]";
    @Column(column = "id_bag")
    private Integer bagId = -1;
    @Column(column = "id_compagny")
    private Integer companyId = -1;
    @Column
    private boolean isInService = false;
    @Column
    private String originalStyle = "";
    @Column
    private double health = 100;
    @Column
    private int commandLevel = 0;
    @Column
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;

    public void setCharacterStyle(CharacterStyle characterStyle) {
        this.characterStyle = new Gson().toJson(characterStyle);
    }

    public CharacterStyle decodeCharacterStyle() {
        if(this.characterStyle.equals("")) return new CharacterStyle();
        return new Gson().fromJson(this.characterStyle, CharacterStyle.class);
    }

    public CharacterStyle decodeOriginalCharacterStyle() {
        if(this.originalStyle.equals("")) return new CharacterStyle();
        return new Gson().fromJson(this.originalStyle, CharacterStyle.class);
    }

    public void setJobLevels(ArrayList<CharacterJobLevel> jobLevels) {
        this.jobLevels = new Gson().toJson(jobLevels);
    }

    public ArrayList<CharacterJobLevel> decodeCharacterJob() {
        if(this.jobLevels.equals("")) return new ArrayList<>();
        return new Gson().fromJson(this.jobLevels, new TypeToken<ArrayList<CharacterJobLevel>>(){}.getType());
    }

    public void save(Player player){
        if(player != null){
            Location loc = player.getLocationAndHeading();
            health = player.getHealth();
            saveX = loc.getX();
            saveY = loc.getY();
            saveZ = loc.getZ();
            saveH = loc.getHeading();
            weapons = new Gson().toJson(WeaponManager.getPlayerWeapons(player));
        }
        save();
    }

}
