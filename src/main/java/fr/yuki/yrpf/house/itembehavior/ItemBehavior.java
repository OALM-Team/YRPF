package fr.yuki.yrpf.house.itembehavior;

import fr.yuki.yrpf.model.HouseItemObject;

public abstract class ItemBehavior {
    protected HouseItemObject houseItemObject;

    public ItemBehavior(HouseItemObject houseItemObject) {
        this.houseItemObject = houseItemObject;
    }

    public abstract void onSpawn();
    public abstract void onDestroy();
    public abstract void onInteract();
}
