package fr.yuki.yrpf.luaapi.vehicle;

import com.google.gson.Gson;
import fr.yuki.yrpf.manager.GarageManager;
import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.VehicleGarage;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

import java.util.ArrayList;

public class GetWorldVehicleByPlayerEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        Player player = Onset.getPlayer(Integer.parseInt(objects[0].toString()));
        Account account = WorldManager.getPlayerAccount(player);
        if(account == null) return false;
        ArrayList<VehicleGarage> vehicleGarages = GarageManager.getVehiclesOutsideGarageByPlayer(player);
        return new Gson().toJson(vehicleGarages);
    }
}
