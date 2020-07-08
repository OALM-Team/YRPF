package fr.yuki.yrpf.net.payload;

public class SetLangPayload {
    private String type;
    private String lang;

    public SetLangPayload(String lang) {
        this.type = "SET_LANG";
        this.lang = lang;
    }

    public String getType() {
        return type;
    }

    public String getLang() {
        return lang;
    }
}
