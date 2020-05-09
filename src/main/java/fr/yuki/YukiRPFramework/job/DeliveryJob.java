package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.enums.JobEnum;
import fr.yuki.YukiRPFramework.job.harvest.DeliveryPackage;
import fr.yuki.YukiRPFramework.job.harvest.Garbage;

import java.util.ArrayList;

public class DeliveryJob extends Job {
    public DeliveryJob() {
        super();
        this.harvestableObjectsTemplate = new ArrayList<>();
        this.harvestableObjectsTemplate.add(new DeliveryPackage());

        if(this.setup()) {
            this.load();
        }

        this.refillHarvestResources();
    }

    @Override
    public JobEnum getJobType() {
        return JobEnum.DELIVERY;
    }

    @Override
    public int getRefillInterval() {
        return 60000*5;
    }
}
