package fr.yuki.yrpf.luaapi.map;

import com.google.gson.Gson;
import fr.yuki.yrpf.manager.MapManager;
import fr.yuki.yrpf.model.GameMapMarker;
import fr.yuki.yrpf.net.payload.AddGameMapMarkerPayload;
import fr.yuki.yrpf.net.payload.RemoveMapMarkerPayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class RemoveMapMarkerEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        GameMapMarker mapMarker = MapManager.getMapConfig().getMarkers().stream()
                .filter(x -> x.getId() == Integer.parseInt(objects[0].toString())).findFirst().orElse(null);
        if(mapMarker == null) return false;
        MapManager.getMapConfig().getMarkers().remove(mapMarker);
        for(Player player : Onset.getPlayers()) {
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new RemoveMapMarkerPayload(mapMarker.getId())));
        }
        return true;
    }
}
