package fr.yuki.YukiRPFramework.manager;

import fr.yuki.YukiRPFramework.dao.ATMDAO;
import fr.yuki.YukiRPFramework.dao.AccountDAO;
import fr.yuki.YukiRPFramework.dao.GarageDAO;
import fr.yuki.YukiRPFramework.dao.VehicleSellerDAO;
import fr.yuki.YukiRPFramework.model.ATM;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.Garage;
import fr.yuki.YukiRPFramework.model.VehicleSeller;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.data.Vector;
import net.onfirenetwork.onsetjava.entity.NPC;
import net.onfirenetwork.onsetjava.entity.Pickup;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.entity.Vehicle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class WorldManager {
    private static HashMap<Integer, Account> accounts;
    private static ArrayList<ATM> atms;
    private static ArrayList<Garage> garages;
    private static ArrayList<VehicleSeller> vehicleSellers;

    /**
     * Init the world manager
     * @throws SQLException
     */
    public static void init() throws SQLException {
        accounts = new HashMap<>();

        // Load atms
        atms = ATMDAO.loadATMs();
        spawnATMs();
        Onset.print("Loaded " + atms.size() + " atm(s) from the database");

        // Load garages
        garages = GarageDAO.loadGarages();
        spawnGarages();
        Onset.print("Loaded " + garages.size() + " garage(s) from the database");

        // Load vehicle sellers
        vehicleSellers = VehicleSellerDAO.loadVehicleSellers();
        Onset.print("Loaded " + vehicleSellers.size() + " vehicle seller(s) from the database");
        spawnVehicleSellers();
    }

    /**
     * Spanw atms in the world
     */
    private static void spawnATMs() {
        for(ATM atm : atms) {
            try  {
                Pickup pickup = Onset.getServer().createPickup(new Vector(atm.getX(), atm.getY(), atm.getZ()-100), 336);
                pickup.setScale(new Vector(1,1,0.1d));
                pickup.setProperty("color", "02e630", true);
                atm.setPickup(pickup);
                Onset.getServer().createText3D("ATM [Utiliser]", 15, atm.getX(), atm.getY(), atm.getZ() + 150, 0 , 0 ,0);
            }
            catch(Exception ex) {
                Onset.print("Can't spawn the atm: " + ex.toString());
            }
        }
    }

    /**
     * Spawns garages in the world
     */
    private static void spawnGarages() {
        for(Garage garage : garages) {
            Pickup pickup = Onset.getServer().createPickup(new Vector(garage.getX(), garage.getY(), garage.getZ()-100), 336);
            pickup.setScale(new Vector(3,3,0.1d));
            pickup.setProperty("color", "6500bd", true);
            Onset.getServer().createText3D("Garage [Utiliser]", 20, garage.getX(), garage.getY(), garage.getZ() + 150, 0 , 0 ,0);
        }
    }

    private static void spawnVehicleSellers() {
        for(VehicleSeller vehicleSeller : vehicleSellers) {
            Pickup pickup = Onset.getServer().createPickup(new Vector(vehicleSeller.getX(), vehicleSeller.getY(),
                    vehicleSeller.getZ()-100), 336);
            pickup.setScale(new Vector(1,1,0.1d));
            pickup.setProperty("color", "ffa600", true);
            Onset.getServer().createText3D("Vendeur " + vehicleSeller.getName() + " [Utiliser]", 20, vehicleSeller.getX(),
                    vehicleSeller.getY(), vehicleSeller.getZ() + 150, 0 , 0 ,0);
            NPC npc = Onset.getServer().createNPC(new Location(vehicleSeller.getX(), vehicleSeller.getY(),
                    vehicleSeller.getZ(), vehicleSeller.getH()));
            npc.setProperty("clothing", vehicleSeller.getNpcClothing(), true);
        }
    }

    /**
     * Find a player by the steam id
     * @param steamId The steam id
     * @return The player
     */
    public static Player findPlayerBySteamId(final String steamId) {
        for(Player p : Onset.getPlayers()) {
            if(p.getSteamId() == steamId) return p;
        }
        return null;
    }

    /**
     * Find a player by the account id
     * @param accountId The account id
     * @return The player
     */
    public static Player findPlayerByAccountId(int accountId) {
        for (Player p : Onset.getPlayers()) {
            if(p.getPropertyInt("accountId") == accountId) {
                return p;
            }
        }
        return null;
    }

    /**
     * Handle the interaction request and send it to all interactible elements
     * @param player The player
     */
    public static void handleInteract(Player player) {
        // Check weared object
        if(CharacterManager.getCharacterStateByPlayer(player).getWearableWorldObject() != null && player.getVehicle() == null) {
            JobManager.handleUnwearObject(player);
            return;
        }

        ATMManager.handleATMInteract(player);
        GarageManager.handleVSellerInteract(player);
        JobManager.tryToHarvest(player);
        if(player.getVehicle() == null) VehicleManager.handleVehicleChestStorageRequest(player);
        GarageManager.handleGarageInteract(player);
    }

    /**
     * Save the player account (position, inventory ..)
     * @param player The player
     */
    public static void savePlayer(Player player) {
        try {
            Account account = getPlayerAccount(player);
            AccountDAO.updateAccount(account, player);
        } catch (Exception ex) {
            Onset.print("Can't save the account: " + ex.toString());
        }
    }

    /**
     * Get the account for the player
     * @param player The player
     * @return The account
     */
    public static Account getPlayerAccount(Player player) {
        return accounts.get(player.getPropertyInt("accountId"));
    }

    public static ArrayList<ATM> getAtms() {
        return atms;
    }

    public static HashMap<Integer, Account> getAccounts() {
        return accounts;
    }

    public static ArrayList<Garage> getGarages() {
        return garages;
    }

    public static ArrayList<VehicleSeller> getVehicleSellers() {
        return vehicleSellers;
    }
}
