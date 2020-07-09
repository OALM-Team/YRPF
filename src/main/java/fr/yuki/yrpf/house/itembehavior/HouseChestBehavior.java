package fr.yuki.yrpf.house.itembehavior;

import fr.yuki.yrpf.inventory.Inventory;
import fr.yuki.yrpf.manager.InventoryManager;
import fr.yuki.yrpf.manager.UIStateManager;
import fr.yuki.yrpf.model.HouseItemObject;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

public class HouseChestBehavior extends ItemBehavior {
    private Inventory inventory;
    private boolean isOpen = false;

    public HouseChestBehavior(HouseItemObject houseItemObject) {
        super(houseItemObject);
    }

    @Override
    public void onSpawn() {
        if(InventoryManager.getInventories().entrySet().stream().filter
                (x -> x.getValue().getHouseItemId() == this.houseItemObject.getId()).findFirst().orElse(null) == null) {
            this.inventory = new Inventory();
            this.inventory.setInventoryType(1);
            this.inventory.setInventoryType(0);
            this.inventory.setCharacterId(-1);
            this.inventory.setVehicleId(-1);
            this.inventory.setHouseItemId(this.houseItemObject.getId());
            this.inventory.save();
            InventoryManager.getInventories().put(this.inventory.getId(), this.inventory);
            Onset.print("Create new inventory for this chest id="+this.houseItemObject.getId());
        } else {
            this.inventory = InventoryManager.getInventories().entrySet().stream().filter
                    (x -> x.getValue().getHouseItemId() == this.houseItemObject.getId()).findFirst().orElse(null).getValue();
            Onset.print("Inventory already exist for chest id="+this.houseItemObject.getId());
        }
        this.inventory.setMaxWeight(150);
        this.inventory.parseContent();
    }

    @Override
    public void onDestroy() {
        this.inventory.delete();
    }

    @Override
    public void onInteract(Player player) {
        Onset.print("Open chest id=" + this.houseItemObject.getId());
        InventoryManager.openTransfertInventory(player, InventoryManager.getMainInventory(player), this.inventory);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
