package fr.yuki.YukiRPFramework.manager;

import fr.yuki.YukiRPFramework.enums.ItemTemplateEnum;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.model.ATM;
import fr.yuki.YukiRPFramework.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

public class ATMManager {
    /**
     * Check the interaction request with a atm, check if is nearby a atm
     * @param player The player
     */
    public static boolean handleATMInteract(Player player) {
        for(ATM atm : WorldManager.getAtms()) {
            if(atm.isNear(player)) {
                openATM(player, atm);
                return true;
            }
        }
        return false;
    }

    /**
     * Open the atm for the player
     * @param player The player
     * @param atm The ATM
     */
    public static void openATM(Player player, ATM atm) {
        if(UIStateManager.handleUIToogle(player, "atm")) {
            SoundManager.playSound3D("sounds/atm_sound_in.mp3", player.getLocation(), 200, 0.2);
        }
    }

    /**
     * Handle deposit request from player
     * @param player The player
     * @param value The amount to deposit
     */
    public static void handleATMDeposit(Player player, int value) {
        Onset.print("Request to deposit money="+value);
        Inventory inventory = InventoryManager.getMainInventory(player);
        if(inventory.getCashAmount() < value) {
            Onset.print("Player don't have the cash required on him amount="+inventory.getCashAmount());
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Vous n'avez pas assez d'argent sur vous pour ça");
            return;
        }

        // Add the bank money and save the player
        inventory.removeItem(inventory.getItemByType(ItemTemplateEnum.CASH.id), value);
        Account account = WorldManager.getPlayerAccount(player);
        account.setBankMoney(account.getBankMoney() + value);
        WorldManager.savePlayer(player);
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Vous avez déposer " + value + "$ sur votre compte banquaire");
    }

    /**
     * Withdraw money from the bank account
     * @param player The player
     * @param value The amount to withdraw
     */
    public static void handleATMWithdraw(Player player, int value) {
        Onset.print("Request to deposit money="+value);
        Inventory inventory = InventoryManager.getMainInventory(player);
        Account account = WorldManager.getPlayerAccount(player);
        if(account.getBankMoney() < value) {
            Onset.print("Player don't have the cash required in bank amount="+inventory.getCashAmount());
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Vous n'avez pas assez d'argent en banque pour ça");
            return;
        }

        // Add the bank money and save the player
        InventoryManager.addItemToPlayer(player, ItemTemplateEnum.CASH.id, value);
        account.setBankMoney(account.getBankMoney() - value);
        WorldManager.savePlayer(player);
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Vous avez retirer " + value + "$ de votre compte banquaire");
    }
}
