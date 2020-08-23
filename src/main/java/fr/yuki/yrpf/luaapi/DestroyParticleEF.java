package fr.yuki.yrpf.luaapi;

import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.modding.WorldParticle;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class DestroyParticleEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        WorldParticle particle = WorldManager.getWorldParticleHashMap().get(Integer.parseInt(objects[0].toString()));
        particle.stop();
        WorldManager.getWorldParticleHashMap().remove(particle.getId());
        return true;
    }
}
