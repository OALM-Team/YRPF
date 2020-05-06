package fr.yuki.YukiRPFramework.enums;

public enum ToastTypeEnum {
    INFO("info"),
    SUCCESS("success"),
    WARN("warn"),
    ERROR("error"),
    DEFAULT("default");

    public final String type;

    private ToastTypeEnum(String type) {
        this.type = type;
    }
}
