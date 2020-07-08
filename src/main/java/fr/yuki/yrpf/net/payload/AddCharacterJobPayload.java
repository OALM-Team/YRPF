package fr.yuki.yrpf.net.payload;

import fr.yuki.yrpf.character.CharacterJobLevel;

public class AddCharacterJobPayload {
    private String type;
    private String jobId;
    private int xp;
    private int level;
    private String levelName;

    public AddCharacterJobPayload(CharacterJobLevel characterJobLevel) {
        this.type = "ADD_CHARACTERJOB_ITEM";
        this.jobId = characterJobLevel.getJobId();
        this.xp = characterJobLevel.getExp();
        this.level = characterJobLevel.getJobLevel().getLevel();
        this.levelName = characterJobLevel.getJobLevel().getName();
    }

    public String getType() {
        return type;
    }

    public String getJobId() {
        return jobId;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public String getLevelName() {
        return levelName;
    }
}
