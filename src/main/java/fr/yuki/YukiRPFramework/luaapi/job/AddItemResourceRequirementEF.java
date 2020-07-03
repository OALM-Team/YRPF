package fr.yuki.YukiRPFramework.luaapi.job;

import fr.yuki.YukiRPFramework.job.Job;
import fr.yuki.YukiRPFramework.job.harvest.CustomHarvestableObject;
import fr.yuki.YukiRPFramework.manager.JobManager;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddItemResourceRequirementEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Job job = JobManager.getJobs().get(objects[0].toString());
        if(job == null) return false;
        CustomHarvestableObject harvestableObject = (CustomHarvestableObject)job.getHarvestableObjectsTemplate().stream().filter(x ->
                x.getName().equals(objects[1].toString())).findFirst().orElse(null);
        if(harvestableObject == null) return false;
        harvestableObject.setRequiredItem(Integer.parseInt(objects[2].toString()));
        return true;
    }
}
