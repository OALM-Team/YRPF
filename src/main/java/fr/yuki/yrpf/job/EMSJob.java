package fr.yuki.yrpf.job;

import fr.yuki.yrpf.enums.JobEnum;

import java.util.ArrayList;

public class EMSJob extends Job {

    public EMSJob() {
        super();
        this.harvestableObjectsTemplate = new ArrayList<>();

        if(this.setup()) {
            this.load();
        }
    }

    @Override
    public String getJobType() {
        return JobEnum.EMS.name();
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
