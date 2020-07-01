package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.manager.ModdingManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.WorldObject;

import java.util.ArrayList;

public class WeedJob extends Job {
    private WorldObject dealerBoat;

    public WeedJob() {
        super();
        this.harvestableObjectsTemplate = new ArrayList<>();

        if(this.setup()) {
            this.load();
        }

        // Create dealer boat
        createDealerBoat();
    }

    private void createDealerBoat() {
        this.dealerBoat = Onset.getServer().createObject(new Vector(35336, 195898, -250), 50006);
        this.dealerBoat.setRotation(new Vector(0, 90, 0));
        ModdingManager.assignCustomModel(this.dealerBoat, 50006);
    }

    @Override
    public String getJobType() {
        return JobEnum.WEED.type;
    }

    @Override
    public int getRefillInterval() {
        return 0;
    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }
}
