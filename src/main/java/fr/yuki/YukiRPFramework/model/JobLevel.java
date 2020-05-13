package fr.yuki.YukiRPFramework.model;

public class JobLevel {
    private int id;
    private String jobId;
    private String name;
    private int level;
    private int expFloor;

    public String getTranslateName() {
        return name.toLowerCase().replaceAll(" ", "_");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExpFloor() {
        return expFloor;
    }

    public void setExpFloor(int expFloor) {
        this.expFloor = expFloor;
    }
}
