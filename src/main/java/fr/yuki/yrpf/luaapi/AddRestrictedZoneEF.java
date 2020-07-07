package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.modding.Line3D;
import fr.yuki.yrpf.world.RestrictedZone;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddRestrictedZoneEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Onset.print("New restricted zone added from lua : " + objects[0].toString());
        RestrictedZone restrictedZone = new RestrictedZone(new Line3D(
                Double.parseDouble(objects[1].toString()),
                Double.parseDouble(objects[2].toString()),
                Double.parseDouble(objects[3].toString()),
                Double.parseDouble(objects[4].toString()),
                Double.parseDouble(objects[5].toString()),
                Double.parseDouble(objects[6].toString()),
                6
        ), objects[0].toString());
        WorldManager.getRestrictedZones().add(restrictedZone);
        return true;
    }
}
