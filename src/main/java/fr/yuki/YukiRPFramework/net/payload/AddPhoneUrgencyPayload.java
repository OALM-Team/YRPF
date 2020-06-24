package fr.yuki.YukiRPFramework.net.payload;

public class AddPhoneUrgencyPayload {
    private final int id;
    private final String from;
    private final String text;
    private final String service;
    private String type;

    public AddPhoneUrgencyPayload(int id, String from, String text, String service) {
        this.type = "ADD_PHONE_URGENCY";
        this.id = id;
        this.from = from;
        this.text = text;
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    public String getService() {
        return service;
    }
}
