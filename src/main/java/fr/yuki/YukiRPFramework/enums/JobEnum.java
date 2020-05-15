package fr.yuki.YukiRPFramework.enums;

public enum JobEnum {
    LUMBERJACK("LUMBERJACK"),
    DELIVERY("DELIVERY"),
    MINER("MINER"),
    FISHER("FISHER"),
    POLICE("POLICE"),
    WEED("WEED"),
    GARBAGE("GARBAGE"),
    EMS("EMS");

    public final String type;
    private JobEnum(String type) {
        this.type = type;
    }
}
