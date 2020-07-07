package fr.yuki.yrpf.vehicle.storeLayout;

import fr.yuki.yrpf.job.WearableWorldObject;
import fr.yuki.yrpf.manager.VehicleManager;
import net.onfirenetwork.onsetjava.entity.Vehicle;

import java.util.ArrayList;

public abstract class VehicleStoreLayout {
    public abstract boolean isAdaptedForModel(int modelId);
    public abstract StoreLayoutTransform getStoreTransform(int index, WearableWorldObject wearableWorldObject);
    public abstract int maxStorageQuantity();
    public abstract int maxLayoutSlot(int modelId);

    public boolean store(Vehicle vehicle, WearableWorldObject wearableWorldObject) {
        ArrayList<WearableWorldObject> wearableWorldObjectsChest = VehicleManager.getVehicleWearableObjects(vehicle);
        if(wearableWorldObjectsChest.size() + 1 > this.maxStorageQuantity()) return false;
        if(wearableWorldObjectsChest.size() + 1 > this.maxLayoutSlot(wearableWorldObject.getModelId())) return false;

        // Find a free slot
        int index = 0;
        for(int i = 0; i < maxLayoutSlot(wearableWorldObject.getModelId()); i++) {
            int finalI = i;
            boolean hasItemOnThisSlot = wearableWorldObjectsChest.stream().filter(x -> x.getVehicleStorageLayoutIndex() == finalI)
                    .findFirst().orElse(null) != null;
            if(!hasItemOnThisSlot) {
                index = i;
                break;
            }
        }

        StoreLayoutTransform storeLayoutTransform = this.getStoreTransform(index, wearableWorldObject);
        if(storeLayoutTransform == null) return false;
        wearableWorldObject.storeInVehicle(vehicle, storeLayoutTransform);
        return true;
    }
}
