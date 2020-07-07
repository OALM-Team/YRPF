package fr.yuki.yrpf.net.payload;

public class SetFoodPayload {
    private String type;
    private int foodState;
    private int drinkState;

    public SetFoodPayload(int foodState, int drinkState) {
        this.foodState = foodState;
        this.drinkState = drinkState;
        this.type = "SET_FOOD";
    }

    public String getType() {
        return type;
    }

    public int getFoodState() {
        return foodState;
    }

    public int getDrinkState() {
        return drinkState;
    }
}
