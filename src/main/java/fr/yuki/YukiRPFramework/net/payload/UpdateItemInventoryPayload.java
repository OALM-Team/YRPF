package fr.yuki.YukiRPFramework.net.payload;

import fr.yuki.YukiRPFramework.inventory.InventoryItem;

public class UpdateItemInventoryPayload {
    public class InventoryItemPayload {
        private String id;
        private int quantity;
        private String itemId;

        public InventoryItemPayload(String id, int quantity, String itemId) {
            this.id = id;
            this.quantity = quantity;
            this.itemId = itemId;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getItemId() {
            return itemId;
        }

        public String getId() {
            return id;
        }
    }

    private String type;
    private InventoryItemPayload item;

    public UpdateItemInventoryPayload(InventoryItem inventoryItem) {
        this.type = "UPDATE_ITEM_INVENTORY";
        this.item = new InventoryItemPayload(inventoryItem.getId(), inventoryItem.getAmount(), inventoryItem.getTemplateId());
    }

    public String getType() {
        return type;
    }

    public InventoryItemPayload getItem() {
        return item;
    }
}
