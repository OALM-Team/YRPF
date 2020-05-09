package fr.yuki.YukiRPFramework.job;

import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryPointConfig {
    private HashMap<String, ArrayList<JobSpawnPosition>> points;


    public HashMap<String, ArrayList<JobSpawnPosition>> getPoints() {
        return points;
    }

    public void setPoints(HashMap<String, ArrayList<JobSpawnPosition>> points) {
        this.points = points;
    }
}
