package fr.yuki.yrpf.i18n;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.net.payload.AddI18NKey;
import fr.yuki.yrpf.net.payload.SetWindowStatePayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class I18n {
    private static HashMap<String, HashMap<String, String>> langs;
    private static HashMap<String, HashMap<String, String>> toSyncKeys;

    public static void init() throws IOException {
        langs = new HashMap<>();
        toSyncKeys = new HashMap<>();
        for (final File fileEntry : new File(WorldManager.getServerConfig().getLangPath()).listFiles()) {
            String fileString = new String(Files.readAllBytes(Paths.get(fileEntry.getPath())), StandardCharsets.UTF_8);

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            langs.put(fileEntry.getName().split("\\.")[0], gson.fromJson(fileString,
                    new TypeToken<HashMap<String, String>>(){}.getType()));
        }
        Onset.print("Loaded " + langs.size() + " i18n file(s)");
    }

    public static String t(String lang, String key, String... params) {
        if(!langs.containsKey(lang)) return key;
        if(!langs.get(lang).containsKey(key)) return key;
        String value = langs.get(lang).get(key);
        for(int i = 0; i < params.length; i++) {
            value = value.replace("{" + i + "}", params[i]);
        }
        return value;
    }

    public static void addKeyToLang(String lang, String key, String value) {
        langs.get(lang).put(key, value);
        if(!toSyncKeys.containsKey(lang)) {
            toSyncKeys.put(lang, new HashMap<>());
        }
        toSyncKeys.get(lang).put(key, value);
        Onset.print("Add custom i18n key lang="+ lang + " key=" + key);
    }

    public static void syncCustomLangs(Player player) {
        for(Map.Entry<String, HashMap<String, String>> langs : toSyncKeys.entrySet()) {
            for(Map.Entry<String, String> keyValue : langs.getValue().entrySet()) {
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddI18NKey
                        (langs.getKey(), keyValue.getKey(), keyValue.getValue())));
            }
        }
    }
}
