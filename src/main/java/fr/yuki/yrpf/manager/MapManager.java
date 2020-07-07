package fr.yuki.yrpf.manager;

import com.google.gson.Gson;
import fr.yuki.yrpf.model.GameMapConfig;
import fr.yuki.yrpf.model.GameMapMarker;
import fr.yuki.yrpf.model.GameMapZone;
import fr.yuki.yrpf.net.payload.AddGameMapMarkerPayload;
import fr.yuki.yrpf.net.payload.AddGameMapZonePayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MapManager {
    private static GameMapConfig mapConfig;

    public static void init() throws FileNotFoundException {
        new File("yrpf").mkdir();
        mapConfig = new Gson().fromJson(new FileReader("yrpf/gamemap.json"), GameMapConfig.class);
        Onset.print("Loaded the gamemap from the JSON config file");
    }

    public static void setupGameMap(Player player) {
        // Send markers
        for(GameMapMarker gameMapMarker : mapConfig.getMarkers()) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddGameMapMarkerPayload(gameMapMarker)));
        }

        // Send zones
        for(GameMapZone gameMapZone : mapConfig.getZones()) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddGameMapZonePayload(gameMapZone)));
        }
    }

    public static GameMapConfig getMapConfig() {
        return mapConfig;
    }
}
