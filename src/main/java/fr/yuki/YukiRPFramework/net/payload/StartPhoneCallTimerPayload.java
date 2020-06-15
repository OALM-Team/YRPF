package fr.yuki.YukiRPFramework.net.payload;

public class StartPhoneCallTimerPayload {
    private String type;

    public StartPhoneCallTimerPayload() {
        this.type = "START_PHONE_CALL_TIMER";
    }

    public String getType() {
        return type;
    }
}
