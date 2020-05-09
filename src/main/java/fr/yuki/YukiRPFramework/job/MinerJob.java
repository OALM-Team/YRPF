package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.job.harvest.CooperOre;
import fr.yuki.YukiRPFramework.job.harvest.DeliveryPackage;

import java.util.ArrayList;

public class MinerJob extends Job {
    public MinerJob() {
        super();
        this.harvestableObjectsTemplate = new ArrayList<>();
        this.harvestableObjectsTemplate.add(new CooperOre());

        if(this.setup()) {
            this.load();
        }

        this.refillHarvestResources();
    }


    @Override
    public JobEnum getJobType() {
        return JobEnum.MINER;
    }

    @Override
    public int getRefillInterval() {
        return 60000 * 10;
    }
}
