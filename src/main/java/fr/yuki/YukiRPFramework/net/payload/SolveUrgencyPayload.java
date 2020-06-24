package fr.yuki.YukiRPFramework.net.payload;

public class SolveUrgencyPayload {
    private String service;
    private int id;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
