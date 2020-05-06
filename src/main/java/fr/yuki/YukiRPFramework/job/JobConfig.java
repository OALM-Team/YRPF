package fr.yuki.YukiRPFramework.job;

import net.onfirenetwork.onsetjava.data.Vector;

import java.util.ArrayList;
import java.util.HashMap;

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
