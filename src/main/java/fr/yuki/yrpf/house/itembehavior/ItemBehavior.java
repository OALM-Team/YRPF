package fr.yuki.yrpf.house.itembehavior;

import fr.yuki.yrpf.model.HouseItemObject;
import net.onfirenetwork.onsetjava.entity.Player;

public abstract class ItemBehavior {
    protected HouseItemObject houseItemObject;

    public ItemBehavior(HouseItemObject houseItemObject) {
        this.houseItemObject = houseItemObject;
    }

    public abstract void onSpawn();
    public abstract void onDestroy();
    public abstract void onInteract(Player player);
    public abstract boolean canBeMove();
}
