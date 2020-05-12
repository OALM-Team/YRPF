package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.job.harvest.Garbage;
import fr.yuki.YukiRPFramework.job.harvest.LumberjackTreeCommon;

import java.util.ArrayList;

public class GarbageJob extends Job {
    public GarbageJob() {
        super();
        this.harvestableObjectsTemplate = new ArrayList<>();
        this.harvestableObjectsTemplate.add(new Garbage());

        if(this.setup()) {
            this.load();
        }

        this.refillHarvestResources();
    }

    @Override
    public JobEnum getJobType() {
        return JobEnum.GARBAGE;
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
