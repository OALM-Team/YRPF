package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.dao.CompagnyDAO;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.Compagny;
import fr.yuki.YukiRPFramework.net.payload.AddCompagnyEmployeePayload;
import fr.yuki.YukiRPFramework.net.payload.ClearCompagnyEmployeesPayload;
import fr.yuki.YukiRPFramework.net.payload.ClearPhoneUrgencyPayload;
import fr.yuki.YukiRPFramework.net.payload.SetCompagnyPayload;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;

public class CompagnyManager {
    public static ArrayList<Compagny> compagnies = new ArrayList<>();

    public static void init() throws SQLException {
        compagnies = CompagnyDAO.loadCompagnies();
        Onset.print("Loaded " + compagnies.size() + " compagnie(s) from the database");
    }

    public static Compagny getCompagnyById(int id) {
        return compagnies.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public static void handleCreateRequest(Player player, String name) throws SQLException {
        Account account = WorldManager.getPlayerAccount(player);
        if(account.getCompagnyId() != -1) return;
        Compagny compagny = new Compagny();
        compagny.setName(name.trim());
        compagny.setBankCash(0);
        compagny.setOwner(player.getSteamId());
        compagny.setMaxMember(5);
        CompagnyDAO.insertCompagny(compagny);
        account.setCompagnyId(compagny.getId());
        WorldManager.savePlayer(player);
        compagnies.add(compagny);
        Onset.print("Compagny " + name + " created");

        refreshCompagny(player);
    }

    public static ArrayList<Player> getOnlineEmployees(Compagny compagny) {
        ArrayList<Player> players = new ArrayList<>();
        for(Player player : Onset.getPlayers()) {
            try {
                Account account = WorldManager.getPlayerAccount(player);
                if(account == null) continue;
                if(account.getCompagnyId() == compagny.getId()) players.add(player);
            } catch (Exception ex) {}
        }
        return players;
    }

    public static void refreshCompagny(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        if(account.getCompagnyId() == -1) { // No compagny
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetCompagnyPayload(
                    -1, "", 0,0
            )));
        } else { // Has compagny
            Compagny compagny = getCompagnyById(account.getCompagnyId());
            if(compagny == null) { // Can't find the compagny
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetCompagnyPayload(
                        -1, "", 0,0
                )));
                return;
            }
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetCompagnyPayload(
                    compagny.getId(), compagny.getName(), compagny.isOwner(player) ? 1:0,compagny.getBankCash()
            )));

            // Refresh employees
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new ClearCompagnyEmployeesPayload()));
            for(Player employee : getOnlineEmployees(compagny)) {
                Account eAccount = WorldManager.getPlayerAccount(employee);
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddCompagnyEmployeePayload(
                        employee.getSteamId(), eAccount.getCharacterName(), compagny.isOwner(employee) ? 1 : 0, true
                )));
            }
        }
    }
}
