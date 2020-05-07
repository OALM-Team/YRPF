package fr.yuki.YukiRPFramework.model;

import java.util.ArrayList;

public class GameMapZone {
    private ArrayList<ArrayList<Integer>> points;
    private String color;

    public ArrayList<ArrayList<Integer>> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<ArrayList<Integer>> points) {
        this.points = points;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
