package fr.yuki.yrpf.phone;

import net.onfirenetwork.onsetjava.data.Vector;

public class UrgencyPhoneMessage extends PhoneMessage {
    private int id;
    private String service;
    private Vector position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }
}
