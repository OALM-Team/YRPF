package fr.yuki.YukiRPFramework.manager;

import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.i18n.I18n;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.model.ItemTemplate;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Weapon;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class WeaponManager {
    public static boolean requestEquipWeapon(Player player, int weaponId) {
        HashMap<Integer, Weapon> weapons = getPlayerWeapons(player);
        int slot = -1;
        if(weapons.get(1).getModel() == 1) {
            slot = 1;
        } else if(weapons.get(2).getModel() == 1) {
            slot = 2;
        } else if(weapons.get(3).getModel() == 1) {
            slot = 3;
        }
        if(slot == -1) return false;
        Onset.print("Set weapon slot=" + slot + ", weaponId=" + weaponId);
        player.setWeapon(slot, weaponId, 0, true, false);
        WorldManager.savePlayer(player);
        return true;
    }

    public static HashMap<Integer, Weapon> getPlayerWeapons(Player player) {
        HashMap<Integer, Weapon> weapons = new HashMap<>();
        weapons.put(1, player.getWeapon(1));
        weapons.put(2, player.getWeapon(2));
        weapons.put(3, player.getWeapon(3));
        return weapons;
    }

    public static boolean fillWeaponWithAmmo(Player player) {
        Weapon weapon = player.getWeapon(player.getWeaponSlot());
        if(weapon.getModel() == 1) return false;
        ItemTemplate itemTemplate = InventoryManager.getItemTemplates().values().stream()
                .filter(x -> x.getWeaponId() == weapon.getModel()).findFirst().orElse(null);
        if(itemTemplate == null) return false;
        weapon.setAmmo(weapon.getAmmo() + itemTemplate.getAmmoPerRecharge());
        player.setWeapon(player.getWeaponSlot(), weapon.getModel(), weapon.getAmmo(), true, false);
        WorldManager.savePlayer(player);
        return true;
    }

    public static void storeWeapon(Player player) {
        Weapon weapon = player.getWeapon(player.getWeaponSlot());
        if(weapon.getModel() == 1) return;
        ItemTemplate itemTemplate = InventoryManager.getItemTemplates().values().stream()
                .filter(x -> x.getWeaponId() == weapon.getModel()).findFirst().orElse(null);
        if(itemTemplate == null) return;

        if(InventoryManager.addItemToPlayer(player, String.valueOf(itemTemplate.getId()), 1) == null) return;
        int ammoBack = (weapon.getAmmo() - (weapon.getAmmo() % itemTemplate.getAmmoPerRecharge())) / itemTemplate.getAmmoPerRecharge();
        player.setWeapon(player.getWeaponSlot(), 1, 0, true, false);
        WorldManager.savePlayer(player);
        if(ammoBack == 0) return;
        if(InventoryManager.addItemToPlayer(player, ItemTemplateEnum.AMMO.id, ammoBack) == null) {
            return;
        }
    }
}