package fr.yuki.yrpf.luaapi.items;

import fr.yuki.yrpf.i18n.I18n;
import fr.yuki.yrpf.manager.InventoryManager;
import fr.yuki.yrpf.manager.ItemManager;
import fr.yuki.yrpf.model.ItemTemplate;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.plugin.ExportFunction;

public class CreateItemTemplateEF implements ExportFunction {
    @Override
    public Object call(Object[] objects) {
        // Cast parameters
        int id = Integer.parseInt(objects[0].toString());
        String name = objects[1].toString();
        String description = objects[2].toString();
        double weight = Double.parseDouble(objects[3].toString());
        String pictureUrl = objects[4].toString();
        int modelId = Integer.parseInt(objects[5].toString());
        double modelScale = Double.parseDouble(objects[6].toString());
        int foodValue = Integer.parseInt(objects[7].toString());
        int drinkValue = Integer.parseInt(objects[8].toString());
        int weaponId = Integer.parseInt(objects[9].toString());
        int ammoPerRecharche = Integer.parseInt(objects[10].toString());
        int maskId = Integer.parseInt(objects[11].toString());

        ItemTemplate itemTemplate = new ItemTemplate();
        itemTemplate.setId(id);
        itemTemplate.setName(name);
        itemTemplate.setDescription(description);
        itemTemplate.setWeight(weight);
        itemTemplate.setItemType(100);
        itemTemplate.setPictureName(pictureUrl);
        itemTemplate.setModelId(modelId);
        itemTemplate.setModelScale(modelScale);
        itemTemplate.setFoodValue(foodValue);
        itemTemplate.setDrinkValue(drinkValue);
        itemTemplate.setWeaponId(weaponId);
        itemTemplate.setAmmoPerRecharge(ammoPerRecharche);
        itemTemplate.setBagId(-1);
        itemTemplate.setMaskId(maskId);
        itemTemplate.setCustom(true);

        ItemManager.getCustomItemPictures().put(itemTemplate.getId(), "../../../../" + itemTemplate.getPictureName());
        InventoryManager.getItemTemplates().put(itemTemplate.getId(), itemTemplate);
        Onset.print("New custom item name="+itemTemplate.getName());

        return null;
    }
}
