package fr.yuki.YukiRPFramework.enums;

public enum  ItemTemplateEnum {
    APPLE("1"),
    CASH("2"),
    VKEY("3"),
    LUMBERJACK_HATCHET_1("4"),
    MINER_PICKAXE_1("5"),
    BANANAS("6"),
    FISHING_ROD("7"),
    SALMON("8");

    public final String id;

    private ItemTemplateEnum(String id) {
        this.id = id;
    }
}
