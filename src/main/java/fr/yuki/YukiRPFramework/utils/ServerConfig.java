package fr.yuki.YukiRPFramework.utils;

public class ServerConfig {
    private double deathRespawnX;
    private double deathRespawnY;
    private double deathRespawnZ;
    private double deathRespawnH;
    private int deathRespawnDelay;
    private int xpRate;
    private String sqlHost;
    private String sqlUsername;
    private String sqlPassword;
    private String sqlDb;

    public double getDeathRespawnX() {
        return deathRespawnX;
    }

    public void setDeathRespawnX(double deathRespawnX) {
        this.deathRespawnX = deathRespawnX;
    }

    public double getDeathRespawnY() {
        return deathRespawnY;
    }

    public void setDeathRespawnY(double deathRespawnY) {
        this.deathRespawnY = deathRespawnY;
    }

    public double getDeathRespawnZ() {
        return deathRespawnZ;
    }

    public void setDeathRespawnZ(double deathRespawnZ) {
        this.deathRespawnZ = deathRespawnZ;
    }

    public double getDeathRespawnH() {
        return deathRespawnH;
    }

    public void setDeathRespawnH(double deathRespawnH) {
        this.deathRespawnH = deathRespawnH;
    }

    public int getDeathRespawnDelay() {
        return deathRespawnDelay;
    }

    public void setDeathRespawnDelay(int deathRespawnDelay) {
        this.deathRespawnDelay = deathRespawnDelay;
    }

    public int getXpRate() {
        return xpRate;
    }

    public void setXpRate(int xpRate) {
        this.xpRate = xpRate;
    }

    public String getSqlHost() {
        return sqlHost;
    }

    public void setSqlHost(String sqlHost) {
        this.sqlHost = sqlHost;
    }

    public String getSqlUsername() {
        return sqlUsername;
    }

    public void setSqlUsername(String sqlUsername) {
        this.sqlUsername = sqlUsername;
    }

    public String getSqlDb() {
        return sqlDb;
    }

    public void setSqlDb(String sqlDb) {
        this.sqlDb = sqlDb;
    }

    public String getSqlPassword() {
        return sqlPassword;
    }

    public void setSqlPassword(String sqlPassword) {
        this.sqlPassword = sqlPassword;
    }
}
