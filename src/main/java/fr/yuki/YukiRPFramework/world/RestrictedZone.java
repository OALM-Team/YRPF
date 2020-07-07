package fr.yuki.YukiRPFramework.world;

import fr.yuki.YukiRPFramework.manager.AccountManager;
import fr.yuki.YukiRPFramework.manager.JobManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.modding.Line3D;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.AccountJobWhitelist;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Door;
import net.onfirenetwork.onsetjava.entity.Player;

public class RestrictedZone {
    private Line3D line3D;
    private String jobRestriction;

    public RestrictedZone(Line3D line3D, String jobRestriction) {
        this.line3D = line3D;
        this.jobRestriction = jobRestriction;
    }

    public boolean canInteractWithDoor(Player player, Door door) {
        // Check if the door is inside the zone
        if(this.line3D.getDoorsInside().stream().filter(x -> x.getId() == door.getId()).findFirst().orElse(null) == null) {
            return true;
        }

        // Check job for this door
        Account account = WorldManager.getPlayerAccount(player);
        AccountJobWhitelist accountJobWhitelist = AccountManager.getAccountJobWhitelists().stream()
                .filter(x -> x.getAccountId() == account.getId() && x.getJobId().equals(this.jobRestriction))
                .findFirst().orElse(null);
        //TODO: Add job level requirement
        if(accountJobWhitelist == null) { // The player doesnt have the job
            return false;
        }

        return true;
    }
}