package fr.yuki.YukiRPFramework.net.payload;

public class AddPhoneConversationPayload {
    private String number;
    private String lastMessage;
    private String type;

    public AddPhoneConversationPayload(String number, String lastMessage) {
        this.number = number;
        this.lastMessage = lastMessage;
        this.type = "ADD_PHONE_CONVERSATIONS";
    }

    public String getNumber() {
        return number;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getType() {
        return type;
    }
}
