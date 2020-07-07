package fr.yuki.yrpf.i18n;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.yuki.yrpf.manager.WorldManager;
import net.onfirenetwork.onsetjava.Onset;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class I18n {
    private static HashMap<String, HashMap<String, String>> langs;

    public static void init() throws IOException {
        langs = new HashMap<>();
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
}
