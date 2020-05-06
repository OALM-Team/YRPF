package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.job.JobConfig;
import fr.yuki.YukiRPFramework.modding.ModdingCustomModel;
import fr.yuki.YukiRPFramework.modding.ModdingFile;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.WorldObject;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ModdingManager {
    private static ModdingFile moddingFile;

    public static void init() throws FileNotFoundException {
        moddingFile = new Gson().fromJson(new FileReader("yrpf/modding.json"), ModdingFile.class);
        Onset.print("Modding file loaded with " + moddingFile.getCustomModels().size() + " custom model(s)");
    }

    public static ModdingFile getModdingFile() {
        return moddingFile;
    }

    public static boolean isCustomModelId(int modelId) {
        return moddingFile.getCustomModels().stream().filter(x -> x.getId() == modelId).findFirst().orElse(null) != null;
    }

    public static void assignCustomModel(WorldObject worldObject, int modelId) {
        ModdingCustomModel moddingCustomModel = moddingFile.getCustomModels().stream().filter(x -> x.getId() == modelId).findFirst().orElse(null);
        if(moddingCustomModel == null) {
            Onset.print("Can't assign custom model, can't find the model: " + modelId);
            return;
        }
        worldObject.setProperty("customModelPath", moddingCustomModel.getPath(), true);
    }
}
