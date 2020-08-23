package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.modding.WorldParticle;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class AddParticleEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Vector position = new Vector(Double.parseDouble(objects[0].toString()), Double.parseDouble(objects[1].toString()),
                Double.parseDouble(objects[2].toString()));
        Vector scale = new Vector(Double.parseDouble(objects[3].toString()), Double.parseDouble(objects[4].toString()),
                Double.parseDouble(objects[5].toString()));
        WorldParticle particle = new WorldParticle(objects[7].toString(),
                position,
                scale, Double.parseDouble(objects[6].toString()));
        particle.start();
        WorldManager.getWorldParticleHashMap().put(particle.getId(), particle);
        return particle.getId();
    }
}
