package fr.yuki.yrpf.job;

import java.util.ArrayList;

public class JobSpawn {
    private String name;
    private ArrayList<JobSpawnPosition> spawns;

    public JobSpawn() {
        this.spawns = new ArrayList<>();
    }

    public JobSpawn(String name, ArrayList<JobSpawnPosition> spawns) {
        this.name = name;
        this.spawns = spawns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<JobSpawnPosition> getSpawns() {
        return spawns;
    }

    public void setSpawns(ArrayList<JobSpawnPosition> spawns) {
        this.spawns = spawns;
    }
}
