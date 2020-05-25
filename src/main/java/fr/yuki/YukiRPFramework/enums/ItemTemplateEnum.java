package fr.yuki.YukiRPFramework.enums;

public enum ItemTemplateEnum {
    APPLE("1"),
    CASH("2"),
    VKEY("3"),
    LUMBERJACK_HATCHET_1("4"),
    MINER_PICKAXE_1("5"),
    BANANAS("6"),
    FISHING_ROD("7"),
    SALMON("8"),
    WEED_SEED("9"),
    TICKET_DELIVERY_GROW_BOX("10"),
    POT("11"),
    WATER("12"),
    WEED("13"),
    TICKET_DELIVERY_GENERATOR("14"),
    JERRICAN_FUEL("15"),
    CUFF("16"),
    DEFIBRILATOR("17"),
    LIGHTSABER("18");

    public final String id;

    private ItemTemplateEnum(String id) {
        this.id = id;
    }
}
