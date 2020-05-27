package fr.yuki.YukiRPFramework.manager;

import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Weapon;
import net.onfirenetwork.onsetjava.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class WeaponManager {
    public static boolean requestEquipWeapon(Player player, int weaponId) {
        HashMap<Integer, Weapon> weapons = getPlayerWeapons(player);
        int slot = -1;
        if(weapons.get(1) != null) {
            slot = 1;
        } else if(weapons.get(2) != null) {
            slot = 2;
        } else if(weapons.get(3) != null) {
            slot = 3;
        }
        if(slot == -1) return false;
        Onset.print("Set weapon slot=" + slot + ", weaponId=" + weaponId);
        player.setWeapon(slot, weaponId, 0, true, false);
        return true;
    }

    public static HashMap<Integer, Weapon> getPlayerWeapons(Player player) {
        HashMap<Integer, Weapon> weapons = new HashMap<>();
        weapons.put(1, player.getWeapon(1));
        weapons.put(2, player.getWeapon(2));
        weapons.put(3, player.getWeapon(3));
        return weapons;
    }

    public static boolean fillWeaponWithAmmo(Player player, int ammoAmount) {
        Weapon weapon = player.getWeapon(player.getWeaponSlot());
        if(weapon.getModel() == -1) return false;
        weapon.setAmmo(weapon.getAmmo() + ammoAmount);
        player.setWeapon(player.getWeaponSlot(), weapon.getModel(), weapon.getAmmo(), true, false);
        return true;
    }
}
