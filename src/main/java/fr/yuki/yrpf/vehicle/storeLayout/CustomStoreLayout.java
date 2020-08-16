package fr.yuki.yrpf.vehicle.storeLayout;

import fr.yuki.yrpf.job.WearableWorldObject;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomStoreLayout extends VehicleStoreLayout {
    private HashMap<Integer, ArrayList<StoreLayoutTransform>> layoutTransforms;
    private int vehicleModelId;

    public CustomStoreLayout(int vehicleModelId) {
        this.vehicleModelId = vehicleModelId;
        this.layoutTransforms = new HashMap<>();
    }

    @Override
    public boolean isAdaptedForModel(int modelId) {
        return this.vehicleModelId == modelId;
    }

    @Override
    public StoreLayoutTransform getStoreTransform(int index, WearableWorldObject wearableWorldObject) {
        if(!this.layoutTransforms.containsKey(wearableWorldObject.getModelId())) return null;
        if(this.layoutTransforms.get(wearableWorldObject.getModelId()).size() <= index){
            Onset.print("Can't find a store layout transform for model="+wearableWorldObject.getModelId());
            return null;
        }
        return this.layoutTransforms.get(wearableWorldObject.getModelId()).get(index);
    }

    @Override
    public int maxStorageQuantity() {
        return 100;
    }

    @Override
    public int maxLayoutSlot(int modelId) {
        if(!this.layoutTransforms.containsKey(modelId)) return 0;
        return this.layoutTransforms.get(modelId).size();
    }

    @Override
    public void addStoreTransform(int modelId, StoreLayoutTransform storeLayoutTransform) {
        if(!this.layoutTransforms.containsKey(modelId)) {
            this.layoutTransforms.put(modelId, new ArrayList<>());
        }
        this.layoutTransforms.get(modelId).add(storeLayoutTransform);
    }
}
