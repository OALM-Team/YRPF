package fr.yuki.YukiRPFramework.job.deliveryPackage;

import fr.yuki.YukiRPFramework.dao.HouseItemDAO;
import fr.yuki.YukiRPFramework.manager.HouseManager;
import fr.yuki.YukiRPFramework.model.House;
import fr.yuki.YukiRPFramework.model.HouseItemObject;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.Player;

import java.sql.SQLException;

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
        return this.modelId;
    }

    @Override
    public int getTimeForDelivery() {
        return 5;
    }

    @Override
    public void onDelivered() {
        HouseItemObject houseItemObject = new HouseItemObject();
        houseItemObject.setId(-1);
        houseItemObject.setModelId(this.modelId);
        houseItemObject.setFunctionId(-1);
        houseItemObject.setX(this.position.getX());
        houseItemObject.setY(this.position.getY());
        houseItemObject.setZ(this.position.getZ());
        houseItemObject.setRx(this.rotation.getX());
        houseItemObject.setRy(this.rotation.getY());
        houseItemObject.setRz(this.rotation.getZ());
        House house = HouseManager.getHouseAtLocation(this.position);
        house.getHouseItemObjects().add(houseItemObject);
        houseItemObject.setHouse(house);

        try {
            HouseItemDAO.insertHouseItem(houseItemObject);
        } catch (SQLException ex) {
            Onset.print("Can't insert the house item: " + ex.toString());
        }

        houseItemObject.spawn();
    }
}
