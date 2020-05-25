package fr.yuki.YukiRPFramework.net.payload;

public class SetHouseInfosPayload {
    private String type;
    private int housePrice;
    private String houseName;

    public SetHouseInfosPayload(int housePrice, String houseName) {
        this.housePrice = housePrice;
        this.houseName = houseName;
        this.type = "SET_HOUSE_INFOS";
    }

    public String getType() {
        return type;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public String getHouseName() {
        return houseName;
    }
}
