package fr.yuki.YukiRPFramework.model;

import fr.yuki.YukiRPFramework.modding.Line3D;

import java.util.ArrayList;

public class House {
    private int id;
    private int accountId;
    private int price;
    private String name;
    private double sx;
    private double sy;
    private double sz;
    private double ex;
    private double ey;
    private double ez;

    private boolean locked = true;
    private ArrayList<Integer> allowedPlayers = new ArrayList<>();
    private ArrayList<HouseItemObject> houseItemObjects = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSx() {
        return sx;
    }

    public void setSx(double sx) {
        this.sx = sx;
    }

    public double getSy() {
        return sy;
    }

    public void setSy(double sy) {
        this.sy = sy;
    }

    public double getSz() {
        return sz;
    }

    public void setSz(double sz) {
        this.sz = sz;
    }

    public double getEx() {
        return ex;
    }

    public void setEx(double ex) {
        this.ex = ex;
    }

    public double getEy() {
        return ey;
    }

    public void setEy(double ey) {
        this.ey = ey;
    }

    public double getEz() {
        return ez;
    }

    public void setEz(double ez) {
        this.ez = ez;
    }

    public Line3D getLine3D() {
       return new Line3D(this.sx, this.sy, this.sz, this.ex, this.ey, this.ez, 5);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public ArrayList<HouseItemObject> getHouseItemObjects() {
        return houseItemObjects;
    }

    public void setHouseItemObjects(ArrayList<HouseItemObject> houseItemObjects) {
        this.houseItemObjects = houseItemObjects;
    }

    public ArrayList<Integer> getAllowedPlayers() {
        return allowedPlayers;
    }

    public void setAllowedPlayers(ArrayList<Integer> allowedPlayers) {
        this.allowedPlayers = allowedPlayers;
    }
}
