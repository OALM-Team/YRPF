package fr.yuki.YukiRPFramework.house.itembehavior;

import fr.yuki.YukiRPFramework.manager.WorldManager;
import fr.yuki.YukiRPFramework.model.ATM;
import fr.yuki.YukiRPFramework.model.HouseItemObject;

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
    public void onInteract() {

    }
}
