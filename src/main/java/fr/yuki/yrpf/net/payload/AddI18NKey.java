package fr.yuki.yrpf.net.payload;

public class AddI18NKey {
    private String type;
    private String lang;
    private String key;
    private String value;

    public AddI18NKey(String lang, String key, String value) {
        this.lang = lang;
        this.key = key;
        this.value = value;
        this.type = "ADD_I18N_KEY";
    }

    public String getType() {
        return type;
    }

    public String getLang() {
        return lang;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
