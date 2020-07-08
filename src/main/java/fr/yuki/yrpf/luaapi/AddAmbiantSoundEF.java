package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.manager.SoundManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddAmbiantSoundEF implements ExportFunction {

    @Override
    public Object call(Object[] objects) {
        String fileName = objects[1].toString();
        String name = objects[0].toString();
        double x = Double.parseDouble(objects[2].toString());
        double y = Double.parseDouble(objects[3].toString());
        double z = Double.parseDouble(objects[4].toString());
        int radius = Integer.parseInt(objects[5].toString());
        double volume = Double.parseDouble(objects[6].toString());
        Onset.print(x + "," + y + ", " + z + ", " + fileName + ", " + name + ", " + radius + ", " + volume);
        SoundManager.createAmbiantSound(name, fileName, new Vector(x,y,z), radius, volume);
        return null;
    }
}
