package fr.yuki.yrpf.enums;

public enum ToastTypeEnum {
    INFO("info"),
    SUCCESS("success"),
    WARN("warn"),
    ERROR("error"),
    DEFAULT("default");

    public final String type;

    ToastTypeEnum(String type) {
        this.type = type;
    }
}
