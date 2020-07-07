package fr.yuki.yrpf.net.payload;

public class AddSellerItemPayload {
    private String type;
    private String id;
    private String name;
    private int price;

    public AddSellerItemPayload(String id, String name, int price) {
        this.type = "ADD_SELLER_ITEM";
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
