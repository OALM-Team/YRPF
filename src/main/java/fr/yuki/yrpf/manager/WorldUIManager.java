package fr.yuki.yrpf.manager;

import fr.yuki.yrpf.world.WorldUI;
import lombok.Getter;

import java.util.ArrayList;

public class WorldUIManager {
    @Getter
    private static ArrayList<WorldUI> worldUIS = new ArrayList<>();

    public static WorldUI findWorldUIByID(int id) {
        return worldUIS.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }
}
