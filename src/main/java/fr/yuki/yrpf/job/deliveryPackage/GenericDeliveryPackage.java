package fr.yuki.yrpf.job.deliveryPackage;

import fr.yuki.yrpf.manager.HouseManager;
import fr.yuki.yrpf.model.House;
import fr.yuki.yrpf.model.HouseItemObject;
import fr.yuki.yrpf.model.ItemShopObject;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

public class GenericDeliveryPackage extends DeliveryPackage {
    private int modelId;

    public GenericDeliveryPackage(Player player, Vector position, Vector rotation, int modelId) {
        super(player, position, rotation);
        this.modelId = modelId;
    }

    @Override
    public Vector getPackageScale() {
        return new Vector(1, 1, 1);
    }

    @Override
    public int getModelId() {
        return 505;
    }

    @Override
    public int getTimeForDelivery() {
        return 10000;
    }

    @Override
    public void onDelivered() {
        ItemShopObject itemShopObject = HouseManager.getItemShopObjects().stream()
                .filter(x -> x.getModelId() == this.modelId).findFirst().orElse(null);

        HouseItemObject houseItemObject = new HouseItemObject();
        houseItemObject.setId(-1);
        houseItemObject.setModelId(this.modelId);
        houseItemObject.setFunctionId(itemShopObject == null ? -1 : itemShopObject.getFunctionId());
        houseItemObject.setPosition(position);
        houseItemObject.setRotation(rotation);
        House house = HouseManager.getHouseAtLocation(this.position);
        house.getHouseItemObjects().add(houseItemObject);
        houseItemObject.setHouse(house);

        houseItemObject.save();

        houseItemObject.spawn();
    }
}
