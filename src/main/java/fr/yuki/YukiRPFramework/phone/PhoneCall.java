package fr.yuki.YukiRPFramework.phone;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.manager.SoundManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.net.payload.SetHouseInfosPayload;
import fr.yuki.YukiRPFramework.net.payload.SetPhoneCallStatePayload;
import fr.yuki.YukiRPFramework.net.payload.StartPhoneCallTimerPayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

public class PhoneCall {
    public static int CURRENT_CALL_ID = 1000;

    private int id;
    private Player caller;
    private Player receiver;
    private int state = 1;

    public PhoneCall(Player caller, Player receiver) {
        this.id = CURRENT_CALL_ID;
        CURRENT_CALL_ID++;
        this.caller = caller;
        this.receiver = receiver;
    }

    public boolean isCaller(Player player) {
        return this.caller.getId() == player.getId();
    }

    public void displayCall() {
        // Start the call
        this.caller.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new StartPhoneCallTimerPayload()));
        this.receiver.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new StartPhoneCallTimerPayload()));

        // Display state
        if(state == 1) { // Pending
            Account receiverAccount = WorldManager.getPlayerAccount(receiver);
            Account callerAccount = WorldManager.getPlayerAccount(caller);

            this.caller.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetPhoneCallStatePayload(1,
                    receiverAccount.getPhoneNumber())));
            this.receiver.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetPhoneCallStatePayload(2,
                    callerAccount.getPhoneNumber())));

            SoundManager.playSound3D("sounds/ringtone.mp3", this.receiver.getLocation(), 300, 0.2);
            SoundManager.playSound3D("sounds/calling_beep.mp3", this.receiver.getLocation(), 300, 0.2);
        }
    }

    public void begin() {
        this.caller.setVoiceChannel(this.id, true);
        this.receiver.setVoiceChannel(this.id, true);
        Onset.print("Voice channel set to id: " + this.id);

        Account receiverAccount = WorldManager.getPlayerAccount(receiver);
        Account callerAccount = WorldManager.getPlayerAccount(caller);
        this.state = 2;
        this.caller.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetPhoneCallStatePayload(3,
                receiverAccount.getPhoneNumber())));
        this.receiver.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetPhoneCallStatePayload(3,
                callerAccount.getPhoneNumber())));
    }

    public void end() {
        if(this.state == 1) { // Pending
            this.caller.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetPhoneCallStatePayload(-1,
                    "")));
            this.receiver.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetPhoneCallStatePayload(-1,
                    "")));

            SoundManager.playSound3D("sounds/call_end.mp3", this.receiver.getLocation(), 300, 0.2);
            SoundManager.playSound3D("sounds/call_end.mp3", this.receiver.getLocation(), 300, 0.2);
        } else if(this.state == 2) { // In call
            this.caller.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetPhoneCallStatePayload(-1,
                    "")));
            this.receiver.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetPhoneCallStatePayload(-1,
                    "")));
            this.caller.setVoiceChannel(this.id, false);
            this.receiver.setVoiceChannel(this.id, false);
            SoundManager.playSound3D("sounds/call_end.mp3", this.receiver.getLocation(), 300, 0.2);
            SoundManager.playSound3D("sounds/call_end.mp3", this.receiver.getLocation(), 300, 0.2);
        }
    }
}
