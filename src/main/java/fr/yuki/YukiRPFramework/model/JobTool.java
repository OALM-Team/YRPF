package fr.yuki.YukiRPFramework.model;

import fr.yuki.YukiRPFramework.job.Job;
import fr.yuki.YukiRPFramework.job.tools.GarbageBin;
import fr.yuki.YukiRPFramework.job.tools.JobToolHandler;
import fr.yuki.YukiRPFramework.job.tools.Sawmill;
import fr.yuki.YukiRPFramework.manager.ModdingManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.WorldObject;

public class JobTool {
    private int id;
    private int modelId;
    private String name;
    private String jobType;
    private int levelRequired;
    private String jobToolType;
    private int reward;
    private double x;
    private double y;
    private double z;
    private double rx;
    private double ry;
    private double rz;
    private double sx;
    private double sy;
    private double sz;
    private WorldObject worldObject;
    private JobToolHandler jobToolHandler;

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public void setLevelRequired(int levelRequired) {
        this.levelRequired = levelRequired;
    }

    public String getJobToolType() {
        return jobToolType;
    }

    public void setJobToolType(String jobToolType) {
        this.jobToolType = jobToolType;
        switch (this.jobToolType) {
            case "sawmill":
                this.jobToolHandler = new Sawmill(this);
                break;

            case "garbage_bin":
                this.jobToolHandler = new GarbageBin(this);
                break;
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }

    public double getRz() {
        return rz;
    }

    public void setRz(double rz) {
        this.rz = rz;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WorldObject getWorldObject() {
        return worldObject;
    }

    public void setWorldObject(WorldObject worldObject) {
        this.worldObject = worldObject;
    }

    public boolean isNear(Player player) {
        return new Vector(this.x, this.y, this.z).distance(player.getLocation()) < 250;
    }

    public void spawn(Job job) {
        this.worldObject = Onset.getServer().createObject(new Vector(this.x, this.y, this.z), this.modelId);
        if(ModdingManager.isCustomModelId(this.modelId)) ModdingManager.assignCustomModel(this.worldObject, this.modelId);
        this.worldObject.setRotation(new Vector(this.rx, this.ry, this.rz));
        this.worldObject.setScale(new Vector(this.sx, this.sy, this.sz));
        Onset.getServer().createText3D(this.name + " [Utiliser]", 20, this.x, this.y, this.z + 200, 0 , 0 ,0);
    }

    public JobToolHandler getJobToolHandler() {
        return jobToolHandler;
    }

    public void setJobToolHandler(JobToolHandler jobToolHandler) {
        this.jobToolHandler = jobToolHandler;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
}
