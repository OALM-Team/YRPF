package fr.yuki.yrpf.house.itembehavior;

import fr.yuki.yrpf.manager.WorldManager;
import fr.yuki.yrpf.model.ATM;
import fr.yuki.yrpf.model.HouseItemObject;
import net.onfirenetwork.onsetjava.entity.Player;

public class ATMBehavior extends ItemBehavior {
    private ATM atm;

    public ATMBehavior(HouseItemObject houseItemObject) {
        super(houseItemObject);
    }

    @Override
    public void onSpawn() {
        this.atm = new ATM();
        atm.setX((float)this.houseItemObject.getX());
        atm.setY((float)this.houseItemObject.getY());
        atm.setZ((float)this.houseItemObject.getZ());
        WorldManager.getAtms().add(atm);
    }

    @Override
    public void onDestroy() {
        WorldManager.getAtms().remove(this.atm);
    }

    @Override
    public void onInteract(Player player) {

    }
}
