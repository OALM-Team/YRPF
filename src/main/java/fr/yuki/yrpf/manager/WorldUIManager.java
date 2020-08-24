package fr.yuki.yrpf.manager;

import fr.yuki.yrpf.world.WorldUI;
import lombok.Getter;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;

public class WorldUIManager {
    @Getter
    private static ArrayList<WorldUI> worldUIS = new ArrayList<>();

    public static WorldUI findWorldUIByID(int id) {
        return worldUIS.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public static void handleRequestSync(Player player, int uiID) {
        Onset.print("Request sync for WUI id="+uiID);
        WorldUI worldUI = findWorldUIByID(uiID);
        if(worldUI == null) return;
        worldUI.sync(player);
    }
}
