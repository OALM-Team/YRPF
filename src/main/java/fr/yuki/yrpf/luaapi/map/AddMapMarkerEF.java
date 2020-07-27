package fr.yuki.yrpf.luaapi.map;

import com.google.gson.Gson;
import fr.yuki.yrpf.manager.MapManager;
import fr.yuki.yrpf.model.GameMapMarker;
import fr.yuki.yrpf.net.payload.AddGameMapMarkerPayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

import java.util.ArrayList;

public class AddMapMarkerEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        GameMapMarker mapMarker = new GameMapMarker();
        mapMarker.setType(objects[0].toString());
        mapMarker.setIcon(objects[1].toString());
        int id = MapManager.getMarkerId();
        MapManager.setMarkerId(MapManager.getMarkerId()+1);
        mapMarker.setId(id);
        mapMarker.setPosition(new ArrayList<>());
        mapMarker.getPosition().add(Integer.parseInt(objects[3].toString()));
        mapMarker.getPosition().add(Integer.parseInt(objects[2].toString()));
        MapManager.getMapConfig().getMarkers().add(mapMarker);

        for(Player player : Onset.getPlayers()) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddGameMapMarkerPayload(mapMarker)));
        }

        return mapMarker.getId();
    }
}
