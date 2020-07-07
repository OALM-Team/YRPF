package fr.yuki.yrpf.net.payload;

public class AddPhoneUrgencyPayload {
    private final int id;
    private final String from;
    private final String text;
    private final String service;
    private final double x;
    private final double y;
    private final double z;
    private String type;

    public AddPhoneUrgencyPayload(int id, String from, String text, String service, double x, double y, double z) {
        this.type = "ADD_PHONE_URGENCY";
        this.id = id;
        this.from = from;
        this.text = text;
        this.service = service;
        this.x = x;
        this.y = y;
        this.z = z;
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
