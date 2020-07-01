package fr.yuki.YukiRPFramework.job;

import fr.yuki.YukiRPFramework.job.harvest.Garbage;

import java.util.ArrayList;

public class CustomJob extends Job {
    private String jobName;

    public CustomJob(String jobName) {
        this.jobName = jobName;
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
        return 0;
    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }
}
