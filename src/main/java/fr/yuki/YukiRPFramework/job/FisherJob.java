package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.job.harvest.CooperOre;
import fr.yuki.YukiRPFramework.job.harvest.Salmon;

import java.util.ArrayList;

public class FisherJob extends Job {
    public FisherJob() {
        super();
        this.harvestableObjectsTemplate = new ArrayList<>();
        this.harvestableObjectsTemplate.add(new Salmon());

        if(this.setup()) {
            this.load();
        }

        this.refillHarvestResources();
    }


    @Override
    public JobEnum getJobType() {
        return JobEnum.FISHER;
    }

    @Override
    public int getRefillInterval() {
        return 60000 * 5;
    }
}
