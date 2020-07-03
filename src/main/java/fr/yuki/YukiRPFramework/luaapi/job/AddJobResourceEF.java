package fr.yuki.YukiRPFramework.luaapi.job;

import fr.yuki.YukiRPFramework.job.Job;
import fr.yuki.YukiRPFramework.job.JobSpawn;
import fr.yuki.YukiRPFramework.job.harvest.CustomHarvestableObject;
import fr.yuki.YukiRPFramework.manager.JobManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddJobResourceEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Job job = JobManager.getJobs().get(objects[0].toString());
        if(job == null) return false;
        CustomHarvestableObject customHarvestableObject = new CustomHarvestableObject(
                objects[1].toString(), Integer.parseInt(objects[2].toString()), Integer.parseInt(objects[3].toString()),
                Integer.parseInt(objects[4].toString()), Integer.parseInt(objects[5].toString()), Integer.parseInt(objects[6].toString())
        );
        job.getHarvestableObjectsTemplate().add(customHarvestableObject);
        if(job.getJobConfig().getResources().stream().filter(x -> x.getName().equals(customHarvestableObject.getName()))
                .findFirst().orElse(null) == null) {
            JobSpawn jobSpawn = new JobSpawn();
            jobSpawn.setName(customHarvestableObject.getName());
            job.getJobConfig().getResources().add(jobSpawn);
        }
        job.refillHarvestResources();

        Onset.print("Add harvestable object for job="+job.getJobType()+" name=" + customHarvestableObject.getName());
        return true;
    }
}
