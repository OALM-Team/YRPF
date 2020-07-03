package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.job.harvest.Garbage;

import java.util.ArrayList;

public class CustomJob extends Job {
    private String jobName;
    private int refillInterval;

    public CustomJob(String jobName, int refillInterval) {
        this.jobName = jobName;
        this.refillInterval = refillInterval;
        this.harvestableObjectsTemplate = new ArrayList<>();
        if(this.setup()) {
            this.load();
        }
    }

    @Override
    public String getJobType() {
        return this.jobName;
    }

    @Override
    public int getRefillInterval() {
        return this.refillInterval;
    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }
}
