package fr.yuki.YukiRPFramework.phone;

import fr.yuki.YukiRPFramework.tebex.responses.TebexPlayer;

public class PhoneCall {
    private TebexPlayer caller;
    private TebexPlayer receiver;

    public PhoneCall(TebexPlayer caller, TebexPlayer receiver) {
        this.caller = caller;
        this.receiver = receiver;
    }
}
