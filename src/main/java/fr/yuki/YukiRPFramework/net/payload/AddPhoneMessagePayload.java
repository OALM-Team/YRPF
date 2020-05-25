package fr.yuki.YukiRPFramework.net.payload;

public class AddPhoneMessagePayload {
    private String type;
    private int messageType;
    private String fromNumber;
    private String message;

    public AddPhoneMessagePayload(int messageType, String fromNumber, String message) {
        this.messageType = messageType;
        this.fromNumber = fromNumber;
        this.message = message;

        this.type = "ADD_PHONE_MESSAGE";
    }

    public String getType() {
        return type;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public String getMessage() {
        return message;
    }
}
