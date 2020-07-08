package fr.yuki.yrpf.ui;

public class GenericMenuItem {
    private String text;
    private String action;

    public GenericMenuItem(String text, String action) {
        this.text = text;
        this.action = action;
    }

    public String getText() {
        return text;
    }

    public String getAction() {
        return action;
    }
}
