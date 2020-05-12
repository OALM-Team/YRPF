package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.enums.JobEnum;

import java.util.ArrayList;

public class PoliceJob extends Job {
    public PoliceJob() {
        super();
        this.harvestableObjectsTemplate = new ArrayList<>();

        if(this.setup()) {
            this.load();
        }
    }

    @Override
    public JobEnum getJobType() {
        return JobEnum.POLICE;
    }

    @Override
    public int getRefillInterval() {
        return 0;
    }

    @Override
    public boolean isWhitelisted() {
        return true;
    }
}
