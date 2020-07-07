package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.job.harvest.CooperOre;
import fr.yuki.YukiRPFramework.job.harvest.DeliveryPackage;
import fr.yuki.YukiRPFramework.job.harvest.IronOre;

import java.util.ArrayList;

public class MinerJob extends Job {
    public MinerJob() {
        super();
        this.harvestableObjectsTemplate = new ArrayList<>();
        this.harvestableObjectsTemplate.add(new CooperOre());
        this.harvestableObjectsTemplate.add(new IronOre());

        if(this.setup()) {
            this.load();
        }

        this.refillHarvestResources();
    }


    @Override
    public String getJobType() {
        return JobEnum.MINER.type;
    }

    @Override
    public int getRefillInterval() {
        return 60000 * 10;
    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }
}
