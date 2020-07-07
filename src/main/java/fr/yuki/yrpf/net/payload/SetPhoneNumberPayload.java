package fr.yuki.yrpf.net.payload;

public class SetPhoneNumberPayload {
    private final String type;
    private String phoneNumber;

    public SetPhoneNumberPayload(String phoneNumber) {
        this.type = "SET_PHONE_NUMBER";
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getType() {
        return type;
    }
}
