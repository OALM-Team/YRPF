package fr.yuki.yrpf.vehicle.storeLayout;

import fr.yuki.yrpf.job.WearableWorldObject;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class GarbageTruckStoreLayout extends VehicleStoreLayout {
    private HashMap<Integer, ArrayList<StoreLayoutTransform>> layoutTransforms;

    public GarbageTruckStoreLayout() {
        this.layoutTransforms = new HashMap<>();

        ArrayList<StoreLayoutTransform> garbageTransform = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            garbageTransform.add(new StoreLayoutTransform(i, new Vector(0,0,100), new Vector(0,0,0), new Vector(1,1,1)));
        }

        this.layoutTransforms.put(514, garbageTransform);
    }

    @Override
    public boolean isAdaptedForModel(int modelId) {
        return modelId == 9;
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
        return 20;
    }

    @Override
    public int maxLayoutSlot(int modelId) {
        if(!this.layoutTransforms.containsKey(modelId)) return 0;
        return this.layoutTransforms.get(modelId).size();
    }
}
