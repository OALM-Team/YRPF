package fr.yuki.YukiRPFramework.vehicle.storeLayout;

import fr.yuki.YukiRPFramework.job.WearableWorldObject;
import net.onfirenetwork.onsetjava.data.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class MiniTruckStoreLayout extends VehicleStoreLayout {
    private HashMap<Integer, ArrayList<StoreLayoutTransform>> layoutTransforms;

    public MiniTruckStoreLayout() {
        this.layoutTransforms = new HashMap<>();

        ArrayList<StoreLayoutTransform> logTransform = new ArrayList<>();
        logTransform.add(new StoreLayoutTransform(0, new Vector(-170,-50,100), new Vector(0,90,0), new Vector(1,1,1)));
        logTransform.add(new StoreLayoutTransform(1, new Vector(-170,10,100), new Vector(0,90,0), new Vector(1,1,1)));
        logTransform.add(new StoreLayoutTransform(2, new Vector(-170,60,100), new Vector(0,90,0), new Vector(1,1,1)));
        logTransform.add(new StoreLayoutTransform(3, new Vector(-170,-30,140), new Vector(0,90,0), new Vector(1,1,1)));
        logTransform.add(new StoreLayoutTransform(4, new Vector(-170,30,140), new Vector(0,90,0), new Vector(1,1,1)));
        logTransform.add(new StoreLayoutTransform(5, new Vector(-170,0,180), new Vector(0,90,0), new Vector(1,1,1)));
        this.layoutTransforms.put(50001, logTransform);
    }

    @Override
    public boolean isAdaptedForModel(int modelId) {
        if(modelId == 22 || modelId == 23) return true;
        return false;
    }

    @Override
    public StoreLayoutTransform getStoreTransform(int index, WearableWorldObject wearableWorldObject) {
        if(this.layoutTransforms.get(wearableWorldObject.getModelId()).size() <= index) return null;
        return this.layoutTransforms.get(wearableWorldObject.getModelId()).get(index);
    }

    @Override
    public int maxStorageQuantity() {
        return 6;
    }

    @Override
    public int maxLayoutSlot(int modelId) {
        return this.layoutTransforms.get(modelId).size();
    }
}
