package fr.yuki.yrpf.job;

import com.google.gson.Gson;
import fr.yuki.yrpf.job.harvest.HarvestableObject;
import fr.yuki.yrpf.manager.JobManager;
import fr.yuki.yrpf.manager.ModdingManager;
import fr.yuki.yrpf.model.JobNPC;
import fr.yuki.yrpf.model.JobTool;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.NPC;
import net.onfirenetwork.onsetjava.entity.WorldObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class Job {
    public abstract String getJobType();
    public abstract int getRefillInterval();
    public abstract boolean isWhitelisted();

    protected ArrayList<HarvestableObject> harvestableObjectsTemplate;
    protected JobConfig jobConfig;
    protected ArrayList<WorldHarvestObject> worldHarvestObjects;
    protected ArrayList<JobTool> jobTools;

    public Job() {
        this.worldHarvestObjects = new ArrayList<>();
        this.spawnNpcs();
        this.spawnTools();

        if(this.getRefillInterval() > 0) {
            Onset.timer(this.getRefillInterval(), () -> {
                refillHarvestResources();
            });
        }
    }

    /**
     * Spawn npcs for this job
     */
    private void spawnNpcs() {
        for(JobNPC jobNPC : JobManager.getJobNPCS().stream().filter(x -> x.getJobId().equals(this.getJobType())).collect(Collectors.toList())) {
            NPC npc = Onset.getServer().createNPC(new Vector(jobNPC.getX(), jobNPC.getY(), jobNPC.getZ()), jobNPC.getH());
            Onset.getServer().createText3D(jobNPC.getName() + " [Utiliser]", 20, jobNPC.getX(),
                    jobNPC.getY(), jobNPC.getZ() + 150, 0 , 0 ,0);
            npc.setProperty("clothing", jobNPC.getNpcClothing(), true);
            npc.setRespawnTime(1);
            npc.setHealth(999999);
            jobNPC.setNpc(npc);
        }
    }

    /**
     * Spawn tools for the job
     */
    private void spawnTools() {
        this.jobTools = new ArrayList<>(JobManager.getJobTools().stream()
                .filter(x -> x.getJobType().equals(this.getJobType())).collect(Collectors.toList()));
        for(JobTool tool : this.jobTools) {
            tool.spawn(this);
        }
    }

    /**
     * Setup the job
     * @return If the job has been setuo
     */
    protected boolean setup() {
        try {
            new File("yrpf").mkdir();
            if(new File("yrpf/" + this.getJobType() + ".json").exists()) return true;
            new File("yrpf/" + this.getJobType() + ".json").createNewFile();
            FileWriter fileWriter = new FileWriter("yrpf/" + this.getJobType() + ".json");

            // Create job config file first
            this.jobConfig = new JobConfig();
            for(HarvestableObject harvestableObject : this.harvestableObjectsTemplate) {
                Onset.print("Add " + harvestableObject.getName() + " to job config file");
                JobSpawn jobSpawn = new JobSpawn();
                jobSpawn.setName(harvestableObject.getName());
                this.jobConfig.getResources().add(jobSpawn);
            }
            fileWriter.write(new Gson().toJson(this.jobConfig));
            fileWriter.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Onset.print("Can't create job cache: " + e.toString());
            return false;
        }
    }

    /**
     * Load resources for the job and the config
     */
    protected void load() {
        try {
            new File("yrpf").mkdir();
            this.jobConfig = new Gson().fromJson(new FileReader("yrpf/" + this.getJobType() + ".json"), JobConfig.class);
            for(JobSpawn jobSpawn : this.jobConfig.getResources()) {
                Onset.print("Loaded " + jobSpawn.getName() + " from the job file " + this.getJobType() + " with " + jobSpawn.getSpawns().size() + " spawn point(s)");
            }

            // Check missing ressources with new update
            for(HarvestableObject harvestableObject : this.harvestableObjectsTemplate) {
                if(this.jobConfig.getResources().stream()
                        .filter(x -> x.getName().equals(harvestableObject.getName())).findFirst().orElse(null) == null) {
                    JobSpawn jobSpawn = new JobSpawn();
                    jobSpawn.setName(harvestableObject.getName());
                    this.jobConfig.getResources().add(jobSpawn);
                    this.saveConfig();
                    Onset.print("Add missing job ressource: " + harvestableObject.getName());
                }
            }
        } catch (Exception e) {
            Onset.print("Can't load job cache: " + e.toString());
        }
    }

    /**
     * Save the job config as a json file
     */
    public void saveConfig() {
        try {
            FileWriter fileWriter = new FileWriter("yrpf/" + this.getJobType() + ".json");
            fileWriter.write(new Gson().toJson(this.jobConfig));
            fileWriter.close();
        } catch (Exception e) {
            Onset.print("Can't save the job config: " + e.toString());
        }
    }

    /**
     * Refill all resources for this job
     */
    public void refillHarvestResources() {
        Onset.print("Refill resources for job=" + this.getJobType());
        for(JobSpawn jobSpawn : this.jobConfig.getResources()) {
            HarvestableObject harvestableObject = this.harvestableObjectsTemplate.stream()
                    .filter(x -> x.getName().equals(jobSpawn.getName())).findFirst().orElse(null);
            for(JobSpawnPosition jobSpawnPosition : jobSpawn.getSpawns()) {
                if(this.worldHarvestObjects.stream().filter(x -> x.getJobSpawnPosition().getUuid().equals(jobSpawnPosition.getUuid()))
                .findFirst().orElse(null) != null) {
                    continue;
                }
                WorldObject worldObject = Onset.getServer().createObject(new Vector(jobSpawnPosition.getX(),
                        jobSpawnPosition.getY(), jobSpawnPosition.getZ() - 100), harvestableObject.getModelId());
                worldObject.setScale(harvestableObject.getScale());
                worldObject.setProperty("harvestable", 1, true);
                worldObject.setProperty("harvestableResourceName", harvestableObject.getName(), true);
                worldObject.setProperty("harvestableInteractDistance", harvestableObject.distanceToInteract(), true);
                if(ModdingManager.isCustomModelId(harvestableObject.getModelId()))
                    ModdingManager.assignCustomModel(worldObject, harvestableObject.getModelId());
                WorldHarvestObject worldHarvestObject = new WorldHarvestObject(jobSpawn, jobSpawnPosition,
                        harvestableObject, worldObject, this);
                this.worldHarvestObjects.add(worldHarvestObject);
            }
        }
    }

    public ArrayList<WorldHarvestObject> getWorldHarvestObjects() {
        return worldHarvestObjects;
    }

    public ArrayList<HarvestableObject> getHarvestableObjectsTemplate() {
        return harvestableObjectsTemplate;
    }

    public JobConfig getJobConfig() {
        return jobConfig;
    }

    public ArrayList<JobTool> getJobTools() {
        return jobTools;
    }
}
