package fr.yuki.yrpf.luaapi.worldui;

import fr.yuki.yrpf.manager.WorldUIManager;
import fr.yuki.yrpf.world.WProgressBarWUI;
import fr.yuki.yrpf.world.WorldUI;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class CreateWorldUIEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Vector position = new Vector(Double.parseDouble(objects[0].toString()), Double.parseDouble(objects[1].toString()),
                Double.parseDouble(objects[2].toString()));
        Vector rotation = new Vector(Double.parseDouble(objects[3].toString()), Double.parseDouble(objects[4].toString()),
                Double.parseDouble(objects[5].toString()));
        WorldUI worldUI = null;
        switch (objects[8].toString()) {
            case "wProgressBar":
                worldUI = new WProgressBarWUI(position, rotation, Integer.parseInt(objects[6].toString()), Integer.parseInt(objects[7].toString()),
                        objects[8].toString());
                break;
        }
        if(worldUI == null) return -1;
        WorldUIManager.getWorldUIS().add(worldUI);
        return worldUI.getId();
    }
}
