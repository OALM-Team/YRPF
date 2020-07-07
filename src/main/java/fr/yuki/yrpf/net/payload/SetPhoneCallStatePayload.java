package fr.yuki.yrpf.net.payload;

public class SetPhoneCallStatePayload {
    private String type;
    private int inCallState;
    private String inCallWith;

    public SetPhoneCallStatePayload(int inCallState, String inCallWith) {
        this.type = "SET_PHONE_CALL_STATE";
        this.inCallState = inCallState;
        this.inCallWith = inCallWith;
    }

    public String getType() {
        return type;
    }

    public int getInCallState() {
        return inCallState;
    }

    public String getInCallWith() {
        return inCallWith;
    }
}
