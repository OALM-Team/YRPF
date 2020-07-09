package fr.yuki.yrpf.net.payload;

import fr.yuki.yrpf.inventory.InventoryItem;

public class AddItemChestPayload {
    public class InventoryItemPayload {
        private String id;
        private int quantity;
        private String itemId;
        private String name;

        public InventoryItemPayload(String id, int quantity, String itemId, String name) {
            this.id = id;
            this.quantity = quantity;
            this.itemId = itemId;
            this.name = name;
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

        public String getName() {
            return name;
        }
    }

    private String type;
    private InventoryItemPayload item;

    public AddItemChestPayload(InventoryItem inventoryItem) {
        this.type = "ADD_ITEM_CHEST";
        this.item = new InventoryItemPayload(inventoryItem.getId(), inventoryItem.getAmount(),
                inventoryItem.getTemplateId(), inventoryItem.getTemplate().getName());
    }

    public String getType() {
        return type;
    }

    public InventoryItemPayload getItem() {
        return item;
    }
}
