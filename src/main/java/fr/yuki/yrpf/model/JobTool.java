package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import fr.yuki.yrpf.job.Job;
import fr.yuki.yrpf.job.tools.*;
import fr.yuki.yrpf.manager.JobManager;
import fr.yuki.yrpf.manager.ModdingManager;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Text3D;
import net.onfirenetwork.onsetjava.entity.WorldObject;

import java.util.UUID;

@Getter @Setter @Table("tbl_job_tool")
public class JobTool extends Model {
    private String uuid;
    @Column(column = "id_job_tool")
    private int id;
    @Column
    private int modelId;
    @Column
    private String name;
    @Column
    private String jobType;
    @Column
    private int levelRequired;
    @Column
    private String jobToolType;
    @Column
    private int reward;
    @Column
    private double x;
    @Column
    private double y;
    @Column
    private double z;
    @Column
    private double rX;
    @Column
    private double rY;
    @Column
    private double rZ;
    @Column
    private double sX;
    @Column
    private double sY;
    @Column
    private double sZ;
    private boolean canShowText = true;

    private WorldObject worldObject;
    private JobToolHandler jobToolHandler;
    private Text3D text3d;

    public JobTool() {
        this.uuid = UUID.randomUUID().toString();
    }

    public String getJobToolType() {
        return jobToolType;
    }

    public void setJobToolType(String jobToolType) {
        this.jobToolType = jobToolType;
        switch (this.jobToolType.toLowerCase()) {
            case "sawmill":
                this.jobToolHandler = new Sawmill(this);
                break;

            case "garbage_bin":
                this.jobToolHandler = new GarbageBin(this);
                break;

            case "growbox":
                this.jobToolHandler = new GrowBox(this);
                break;

            case "generator":
                this.jobToolHandler = new Generator(this);
                break;

            case "ore_furnace":
                this.jobToolHandler = new OreFurnace(this);
                break;
        }
    }

    public WorldObject getWorldObject() {
        return worldObject;
    }

    public void setWorldObject(WorldObject worldObject) {
        this.worldObject = worldObject;
    }

    public Vector getPosition() {
        return new Vector(this.x, this.y, this.z);
    }

    public boolean isNear(Player player) {
        return new Vector(this.x, this.y, this.z).distance(player.getLocation()) < 250;
    }

    public void spawn(Job job) {
        this.worldObject = Onset.getServer().createObject(new Vector(this.x, this.y, this.z), this.modelId);
        if(ModdingManager.isCustomModelId(this.modelId)) ModdingManager.assignCustomModel(this.worldObject, this.modelId);
        this.worldObject.setRotation(new Vector(this.rX, this.rY, this.rZ));
        this.worldObject.setScale(new Vector(this.sX, this.sY, this.sZ));
        this.worldObject.setProperty("isJobTool", "1", true);
        if(this.canShowText) this.text3d = Onset.getServer().createText3D(this.name + " [Utiliser]", 20, this.x, this.y, this.z + 200, 0 , 0 ,0);
        if(this.jobToolHandler != null) {
            if(this.jobToolHandler.canBeUse()) {
                this.setUsable();
            }
        }
    }

    public void setUsable() {
        this.getWorldObject().setProperty("isJobTool", 1, true);
        this.getWorldObject().setProperty("uuid", this.getUuid(), true);
    }

    public JobToolHandler getJobToolHandler() {
        return jobToolHandler;
    }

    public void setJobToolHandler(JobToolHandler jobToolHandler) {
        this.jobToolHandler = jobToolHandler;
    }

    public void destroy() {
        this.worldObject.destroy();
        if(this.canShowText) this.text3d.destroy();
        JobManager.getJobTools().remove(this);
    }
}
