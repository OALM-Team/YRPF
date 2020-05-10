package fr.yuki.YukiRPFramework.model;

import fr.yuki.YukiRPFramework.inventory.InventoryItem;
import fr.yuki.YukiRPFramework.manager.CharacterManager;
import fr.yuki.YukiRPFramework.manager.InventoryManager;
import fr.yuki.YukiRPFramework.manager.ModdingManager;
import fr.yuki.YukiRPFramework.manager.WorldManager;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Pickup;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Text3D;
import net.onfirenetwork.onsetjava.enums.Animation;

public class GroundItem {
    private InventoryItem inventoryItem;
    private Vector position;
    private int quantity;
    private Pickup pickup;
    private Text3D text3D;
    private boolean isAvailable = true;

    public GroundItem(InventoryItem inventoryItem, int quantity, Vector position) {
        this.inventoryItem = inventoryItem;
        this.position = position;
        this.quantity = quantity;
        this.spawn();
    }

    private void spawn() {
        this.pickup = Onset.getServer().createPickup(this.position,
                ModdingManager.isCustomModelId(this.inventoryItem.getTemplate().getModelId()) ? 1 : this.inventoryItem.getTemplate().getModelId());
        this.pickup.setScale(new Vector(this.inventoryItem.getTemplate().getModelScale(), this.inventoryItem.getTemplate().getModelScale(),
                this.inventoryItem.getTemplate().getModelScale()));
        this.text3D = Onset.getServer().createText3D("x" + this.quantity + " [" + this.inventoryItem.getTemplate().getName() + "]",
                12, new Vector(this.position.getX(), this.position.getY(), this.position.getZ() + 90),
                new Vector(0, 0, 0));
        if(ModdingManager.isCustomModelId(this.inventoryItem.getTemplate().getModelId()))
            ModdingManager.assignCustomModel(this.pickup, this.inventoryItem.getTemplate().getModelId());
    }

    public void pickByPlayer(Player player) {
        if(!this.isAvailable) return;
        this.isAvailable = false;
        CharacterManager.setCharacterFreeze(player, true);
        player.setAnimation(Animation.PICKUP_LOWER);
        InventoryManager.getMainInventory(player).addItem(inventoryItem);
        WorldManager.getGroundItems().remove(this);
        InventoryManager.getMainInventory(player).save();
        Onset.delay(1000, () -> {
            CharacterManager.setCharacterFreeze(player, false);
            this.pickup.destroy();
            this.text3D.destroy();
        });
    }

    public void destroy() {

    }

    public boolean isNear(Player player) {
        return this.position.distance(player.getLocation()) < 150;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public Vector getPosition() {
        return position;
    }

    public int getQuantity() {
        return quantity;
    }
}
