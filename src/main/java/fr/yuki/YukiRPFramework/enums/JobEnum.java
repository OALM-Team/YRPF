package fr.yuki.YukiRPFramework.enums;

public enum JobEnum {
    LUMBERJACK("LUMBERJACK"),
    DELIVERY("DELIVERY"),
    MINER("MINER"),
    FISHER("FISHER"),
    GARBAGE("GARBAGE");

    public final String type;
    private JobEnum(String type) {
        this.type = type;
    }
}
