package fr.yuki.YukiRPFramework.house.itembehavior;

import fr.yuki.YukiRPFramework.model.HouseItemObject;

public abstract class ItemBehavior {
    protected HouseItemObject houseItemObject;

    public ItemBehavior(HouseItemObject houseItemObject) {
        this.houseItemObject = houseItemObject;
    }

    public abstract void onSpawn();
    public abstract void onDestroy();
    public abstract void onInteract();
}
