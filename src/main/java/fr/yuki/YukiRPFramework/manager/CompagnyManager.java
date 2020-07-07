package fr.yuki.YukiRPFramework.manager;

import com.google.gson.Gson;
import fr.yuki.YukiRPFramework.character.CharacterState;
import fr.yuki.YukiRPFramework.dao.CompagnyDAO;
import fr.yuki.YukiRPFramework.enums.ToastTypeEnum;
import fr.yuki.YukiRPFramework.model.Account;
import fr.yuki.YukiRPFramework.model.Compagny;
import fr.yuki.YukiRPFramework.net.payload.AddCompagnyEmployeePayload;
import fr.yuki.YukiRPFramework.net.payload.ClearCompagnyEmployeesPayload;
import fr.yuki.YukiRPFramework.net.payload.SetCompagnyPayload;
import fr.yuki.YukiRPFramework.ui.GenericMenu;
import fr.yuki.YukiRPFramework.ui.GenericMenuItem;
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

    public static Compagny getCompagnyByName(String name) {
        return compagnies.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
    }

    public static void destroyCompagny() {

    }

    public static void handleCreateRequest(Player player, String name) throws SQLException {
        Account account = WorldManager.getPlayerAccount(player);
        if(account.getCompanyId() != -1) return;
        if(getCompagnyByName(name.trim()) != null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Ce nom est déjà pris");
            return;
        }
        if(account.getBankMoney() < 10000) { // TODO: Put this in config
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Pas assez d'argent en banque");
            return;
        }
        ATMManager.removeCashFromBank(player, 10000);
        Compagny compagny = new Compagny();
        compagny.setName(name.trim());
        compagny.setBankCash(0);
        compagny.setOwner(player.getSteamId());
        compagny.setMaxMember(5);
        CompagnyDAO.insertCompagny(compagny);
        account.setCompanyId(compagny.getId());
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
                if(account.getCompanyId() == compagny.getId()) players.add(player);
            } catch (Exception ex) {}
        }
        return players;
    }

    public static void refreshCompagny(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        if(account.getCompanyId() == -1) { // No compagny
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetCompagnyPayload(
                    -1, "", 0,0
            )));
        } else { // Has compagny
            Compagny compagny = getCompagnyById(account.getCompanyId());
            if(compagny == null) { // Can't find the compagny
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetCompagnyPayload(
                        -1, "", 0,0
                )));
                account.setCompanyId(-1);
                account.save();
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Votre entreprise n'existe plus");
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

    public static void handleInviteEmployee(Player player, String phoneNumber) {
        Player invitedPlayer = WorldManager.getPlayerByPhoneNumber(phoneNumber);
        if(invitedPlayer == null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Cette personne n'est pas en ligne ou n'existe pas");
            return;
        }
        Account account = WorldManager.getPlayerAccount(player);
        Compagny compagny = getCompagnyById(account.getCompanyId());
        if(compagny == null) return;
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Invitation envoyée");
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(invitedPlayer);
        characterState.setCurrentCompagnyInvited(compagny.getId());

        // Build generic menu
        GenericMenu genericMenu = new GenericMenu(invitedPlayer);
        genericMenu.getItems().add(new GenericMenuItem("Voulez-vous rejoindre " + compagny.getName() + " ?", ""));
        genericMenu.getItems().add(new GenericMenuItem("Accepter", "window.CallEvent(\"RemoteCallInterface\", \"Compagny:AcceptInvitation\");"));
        genericMenu.getItems().add(new GenericMenuItem("Refuser", "window.CallEvent(\"RemoteCallInterface\", \"Compagny:DeclineInvitation\");"));
        genericMenu.show();
        characterState.setCurrentGenericMenu(genericMenu);
    }

    public static void handleAcceptInvitation(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        Compagny compagny = getCompagnyById(characterState.getCurrentCompagnyInvited());
        account.setCompanyId(compagny.getId());
        WorldManager.savePlayer(player);

        // Reset invitation
        characterState.setCurrentCompagnyInvited(-1);
        if(characterState.getCurrentGenericMenu() != null) {
            characterState.getCurrentGenericMenu().hide();
            characterState.setCurrentGenericMenu(null);
        }

        for (Player employee : getOnlineEmployees(compagny)) {
            refreshCompagny(employee);
            UIStateManager.sendNotification(employee, ToastTypeEnum.SUCCESS, account.getCharacterName() + " fait désormais partie de l'entreprise");
        }
    }

    public static void handleDeclineInvitation(Player player) {
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);

        // Reset invitation
        characterState.setCurrentCompagnyInvited(-1);
        if(characterState.getCurrentGenericMenu() != null) {
            characterState.getCurrentGenericMenu().hide();
            characterState.setCurrentGenericMenu(null);
        }
    }

    public static void handleKickEmployee(Player player, String steamid) {
        Account account = WorldManager.getPlayerAccount(player);
        Compagny compagny = getCompagnyById(account.getCompanyId());
        if(!compagny.isOwner(player)) return;
        Player targetPlayer = WorldManager.findPlayerBySteamId(steamid);
        if(targetPlayer == null) return;
        Account targetAccount = WorldManager.getPlayerAccount(targetPlayer);
        if(targetAccount.getId() == account.getId() && compagny.isOwner(player)) {
            UIStateManager.sendNotification(targetPlayer, ToastTypeEnum.ERROR, "Action impossible, contactez un administrateur pour supprimer l'entreprise");
            return;
        }
        targetAccount.setCompanyId(-1);
        WorldManager.savePlayer(targetPlayer);

        for (Player employee : getOnlineEmployees(compagny)) {
            refreshCompagny(employee);
            UIStateManager.sendNotification(employee, ToastTypeEnum.WARN, targetAccount.getCharacterName() + " fait désormais plus partie de l'entreprise");
        }
        UIStateManager.sendNotification(targetPlayer, ToastTypeEnum.ERROR, "Vous avez été licencier de l'entreprise " + compagny.getName());
        refreshCompagny(targetPlayer);
    }
}
