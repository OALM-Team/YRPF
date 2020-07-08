package fr.yuki.yrpf.model;

import java.util.ArrayList;

public class GameMapConfig {
    private ArrayList<GameMapMarker> markers;
    private ArrayList<GameMapZone> zones;

    public ArrayList<GameMapMarker> getMarkers() {
        return markers;
    }

    public void setMarkers(ArrayList<GameMapMarker> markers) {
        this.markers = markers;
    }

    public ArrayList<GameMapZone> getZones() {
        return zones;
    }

    public void setZones(ArrayList<GameMapZone> zones) {
        this.zones = zones;
    }
}
