package fr.yuki.yrpf.luaapi.job;

import com.google.gson.Gson;
import fr.yuki.yrpf.character.CharacterToolAnimation;
import fr.yuki.yrpf.job.WearableWorldObject;
import fr.yuki.yrpf.manager.JobManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.enums.Animation;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class SpawnWearableObjectEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        // Parse parameters
        try {
            int modelId = Integer.parseInt(objects[0].toString());
            String animation = objects[1].toString();
            Vector position = new Vector(Double.parseDouble(objects[2].toString()), Double.parseDouble(objects[3].toString()),
                    Double.parseDouble(objects[4].toString()));
            Vector positionWear = new Vector(Double.parseDouble(objects[5].toString()), Double.parseDouble(objects[6].toString()),
                    Double.parseDouble(objects[7].toString()));
            Vector rotationWear = new Vector(Double.parseDouble(objects[8].toString()), Double.parseDouble(objects[9].toString()),
                    Double.parseDouble(objects[10].toString()));
            Vector scaleWear = new Vector(Double.parseDouble(objects[11].toString()), Double.parseDouble(objects[12].toString()),
                    Double.parseDouble(objects[13].toString()));
            String attachSocket = objects[14].toString();

            WearableWorldObject wearableWorldObject = new WearableWorldObject(modelId,
                    true, Animation.valueOf(animation),
                    new CharacterToolAnimation(modelId, positionWear, rotationWear, scaleWear, attachSocket),
                    position);
            JobManager.getWearableWorldObjects().add(wearableWorldObject);
            return wearableWorldObject.getUuid();
        }catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
