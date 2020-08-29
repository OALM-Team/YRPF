package fr.yuki.yrpf.luaapi.job;

import fr.yuki.yrpf.manager.JobManager;
import fr.yuki.yrpf.manager.VehicleManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Color;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class SpawnRentalVehicleEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        if(player == null) return false;
        Account account = WorldManager.getPlayerAccount(player);
        if(account == null) return false;

        JobManager.destroyRentalVehiclesForPlayer(player);

        VehicleManager.CreateVehicleResult result = VehicleManager.createVehicle(Integer.parseInt(objects[1].toString()),
                new Vector(Double.parseDouble(objects[2].toString()), Double.parseDouble(objects[3].toString()),
                        Double.parseDouble(objects[4].toString())),
                Double.parseDouble(objects[5].toString()), player, null, true);
        java.awt.Color color = java.awt.Color.decode(objects[6].toString());
        result.getVehicle().setColor(new Color(color.getRed(), color.getGreen(), color.getBlue()));
        return result.getVehicle().getId();
    }
}
