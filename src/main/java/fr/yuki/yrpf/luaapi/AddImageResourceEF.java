package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.manager.ModdingManager;
import fr.yuki.yrpf.net.payload.AddImageResourcePayload;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddImageResourceEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        ModdingManager.getImageResourcePayloads().add(new AddImageResourcePayload(
                objects[0].toString(),
                objects[1].toString(),
                "../../../../" +objects[2].toString()
        ));
        return true;
    }
}
