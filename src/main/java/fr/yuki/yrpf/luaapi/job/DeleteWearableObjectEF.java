package fr.yuki.yrpf.luaapi.job;

import fr.yuki.yrpf.job.WearableWorldObject;
import fr.yuki.yrpf.manager.JobManager;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class DeleteWearableObjectEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        String uuid = objects[0].toString();
        WearableWorldObject wearableWorldObject = JobManager.getWearableWorldObjects().stream()
                .filter(x -> x.getUuid().equals(uuid)).findFirst().orElse(null);
        if(wearableWorldObject == null) return false;
        wearableWorldObject.deleteObject();
        return true;
    }
}
