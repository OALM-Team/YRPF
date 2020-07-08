package fr.yuki.yrpf.job;

import fr.yuki.yrpf.enums.JobEnum;
import fr.yuki.yrpf.job.harvest.Salmon;
import fr.yuki.yrpf.job.harvest.Turtle;

import java.util.ArrayList;

public class FisherJob extends Job {
    public FisherJob() {
        super();
        this.harvestableObjectsTemplate = new ArrayList<>();
        this.harvestableObjectsTemplate.add(new Salmon());
        this.harvestableObjectsTemplate.add(new Turtle());

        if(this.setup()) {
            this.load();
        }

        this.refillHarvestResources();
    }


    @Override
    public String getJobType() {
        return JobEnum.FISHER.name();
    }

    @Override
    public int getRefillInterval() {
        return 60000 * 3;
    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }
}
