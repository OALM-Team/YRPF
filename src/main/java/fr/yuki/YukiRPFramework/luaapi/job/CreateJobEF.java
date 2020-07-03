package fr.yuki.YukiRPFramework.luaapi.job;

import fr.yuki.YukiRPFramework.job.CustomJob;
import fr.yuki.YukiRPFramework.manager.JobManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class CreateJobEF implements ExportFunction {

    @Override
    public Object call(Object[] objects) {
        CustomJob customJob = new CustomJob(objects[0].toString(), Integer.parseInt(objects[1].toString()));
        JobManager.getJobs().put(objects[0].toString(), customJob);
        Onset.print("New job created from lua name="+customJob.getJobType());
        return null;
    }
}
