package fr.yuki.yrpf.job;

import fr.yuki.yrpf.enums.JobEnum;
import fr.yuki.yrpf.job.harvest.Garbage;

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
    public String getJobType() {
        return JobEnum.GARBAGE.type;
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
