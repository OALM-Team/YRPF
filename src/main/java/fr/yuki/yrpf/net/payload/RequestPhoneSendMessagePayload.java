package fr.yuki.yrpf.net.payload;

public class RequestPhoneSendMessagePayload {
    private String number;
    private String message;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
