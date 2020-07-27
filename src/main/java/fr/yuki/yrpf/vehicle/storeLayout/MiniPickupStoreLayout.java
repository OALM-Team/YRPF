package fr.yuki.yrpf.vehicle.storeLayout;

import fr.yuki.yrpf.job.WearableWorldObject;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class MiniPickupStoreLayout extends VehicleStoreLayout {
    private HashMap<Integer, ArrayList<StoreLayoutTransform>> layoutTransforms;

    public MiniPickupStoreLayout() {
        this.layoutTransforms = new HashMap<>();

        ArrayList<StoreLayoutTransform> packageTransform = new ArrayList<>();
        packageTransform.add(new StoreLayoutTransform(0, new Vector(-80,40,70), new Vector(0,90,0), new Vector(1,1,1)));
        packageTransform.add(new StoreLayoutTransform(1, new Vector(-80,-30,70), new Vector(0,90,0), new Vector(1,1,1)));
        packageTransform.add(new StoreLayoutTransform(2, new Vector(-185,-30,70), new Vector(0,90,0), new Vector(1,1,1)));
        packageTransform.add(new StoreLayoutTransform(3, new Vector(-185,40,70), new Vector(0,90,0), new Vector(1,1,1)));
        this.layoutTransforms.put(508, packageTransform);

        ArrayList<StoreLayoutTransform> oreTransform = new ArrayList<>();
        oreTransform.add(new StoreLayoutTransform(0, new Vector(-80,40,70), new Vector(0,90,0), new Vector(0.60,0.60,0.60)));
        oreTransform.add(new StoreLayoutTransform(1, new Vector(-80,-30,70), new Vector(0,90,0), new Vector(0.60,0.60,0.60)));
        oreTransform.add(new StoreLayoutTransform(2, new Vector(-185,-30,70), new Vector(0,90,0), new Vector(0.60,0.60,0.60)));
        oreTransform.add(new StoreLayoutTransform(3, new Vector(-185,40,70), new Vector(0,90,0), new Vector(0.60,0.60,0.60)));
        oreTransform.add(new StoreLayoutTransform(4, new Vector(-120,40,110), new Vector(0,90,0), new Vector(0.60,0.60,0.60)));
        oreTransform.add(new StoreLayoutTransform(5, new Vector(-120,-30,110), new Vector(0,90,0), new Vector(0.60,0.60,0.60)));
        oreTransform.add(new StoreLayoutTransform(6, new Vector(-160,0,115), new Vector(0,90,0), new Vector(0.60,0.60,0.60)));
        this.layoutTransforms.put(156, oreTransform);

        ArrayList<StoreLayoutTransform> cooperTransform = new ArrayList<>();
        cooperTransform.add(new StoreLayoutTransform(0, new Vector(-40,5,70),
                new Vector(0,90,0), new Vector(0.5,0.5,0.5)));
        cooperTransform.add(new StoreLayoutTransform(1, new Vector(-70,5,70),
                new Vector(0,90,0), new Vector(0.5,0.5,0.5)));
        cooperTransform.add(new StoreLayoutTransform(2, new Vector(-90,5,70),
                new Vector(0,90,0), new Vector(0.5,0.5,0.5)));
        cooperTransform.add(new StoreLayoutTransform(3, new Vector(-110,5,70),
                new Vector(0,90,0), new Vector(0.5,0.5,0.5)));
        cooperTransform.add(new StoreLayoutTransform(4, new Vector(-130,5,70),
                new Vector(0,90,0), new Vector(0.5,0.5,0.5)));
        cooperTransform.add(new StoreLayoutTransform(5, new Vector(-150,5,70),
                new Vector(0,90,0), new Vector(0.5,0.5,0.5)));
        cooperTransform.add(new StoreLayoutTransform(6, new Vector(-170,5,70),
                new Vector(0,90,0), new Vector(0.5,0.5,0.5)));
        this.layoutTransforms.put(50068, cooperTransform);
    }

    @Override
    public boolean isAdaptedForModel(int modelId) {
        return modelId == 27;
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
        return 7;
    }

    @Override
    public int maxLayoutSlot(int modelId) {
        if(!this.layoutTransforms.containsKey(modelId)) return 0;
        return this.layoutTransforms.get(modelId).size();
    }
}
