package fr.yuki.yrpf.tebex.responses;


public class Account {
    private long id;
    private String domain;
    private String name;
    private Currency currency;
    private boolean onlineMode;
    private String gameType;
    private boolean logEvents;

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getDomain() { return domain; }
    public void setDomain(String value) { this.domain = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency value) { this.currency = value; }

    public boolean getOnlineMode() { return onlineMode; }
    public void setOnlineMode(boolean value) { this.onlineMode = value; }

    public String getGameType() { return gameType; }
    public void setGameType(String value) { this.gameType = value; }

    public boolean getLogEvents() { return logEvents; }
    public void setLogEvents(boolean value) { this.logEvents = value; }
}