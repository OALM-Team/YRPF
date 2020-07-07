package fr.yuki.yrpf.job;

import java.util.ArrayList;

public class JobConfig {
    private ArrayList<JobSpawn> resources;

    public JobConfig() {
        this.resources = new ArrayList<>();
    }

    public ArrayList<JobSpawn> getResources() {
        return resources;
    }

    public void setResources(ArrayList<JobSpawn> resources) {
        this.resources = resources;
    }
}
