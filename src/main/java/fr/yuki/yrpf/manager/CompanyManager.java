package fr.yuki.yrpf.manager;

import com.google.gson.Gson;
import eu.bebendorf.ajorm.Repo;
import fr.yuki.yrpf.character.CharacterState;
import fr.yuki.yrpf.enums.ToastTypeEnum;
import fr.yuki.yrpf.model.Account;
import fr.yuki.yrpf.model.Company;
import fr.yuki.yrpf.net.payload.AddCompagnyEmployeePayload;
import fr.yuki.yrpf.net.payload.ClearCompagnyEmployeesPayload;
import fr.yuki.yrpf.net.payload.SetCompagnyPayload;
import fr.yuki.yrpf.ui.GenericMenu;
import fr.yuki.yrpf.ui.GenericMenuItem;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyManager {
    public static List<Company> companies = new ArrayList<>();

    public static void init() {
        companies = Repo.get(Company.class).all();
        Onset.print("Loaded " + companies.size() + " companies from the database");
    }

    public static Company getCompanyById(int id) {
        return companies.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public static Company getCompanyByName(String name) {
        return companies.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
    }

    public static void handleCreateRequest(Player player, String name) throws SQLException {
        Account account = WorldManager.getPlayerAccount(player);
        if(account.getCompanyId() != -1) return;
        if(getCompanyByName(name.trim()) != null) {
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Ce nom est déjà pris");
            return;
        }
        if(account.getBankMoney() < 10000) { // TODO: Put this in config
            UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Pas assez d'argent en banque");
            return;
        }
        ATMManager.removeCashFromBank(player, 10000);
        Company company = new Company();
        company.setName(name.trim());
        company.setOwner(player.getSteamId());
        company.save();
        account.setCompanyId(company.getId());
        WorldManager.savePlayer(player);
        companies.add(company);
        Onset.print("Company " + name + " created");

        refreshCompany(player);
    }

    public static ArrayList<Player> getOnlineEmployees(Company company) {
        ArrayList<Player> players = new ArrayList<>();
        for(Player player : Onset.getPlayers()) {
            try {
                Account account = WorldManager.getPlayerAccount(player);
                if(account == null) continue;
                if(account.getCompanyId() == company.getId()) players.add(player);
            } catch (Exception ex) {}
        }
        return players;
    }

    public static void refreshCompany(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        if(account.getCompanyId() == -1) { // No compagny
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetCompagnyPayload(
                    -1, "", 0,0
            )));
        } else { // Has compagny
            Company company = getCompanyById(account.getCompanyId());
            if(company == null) { // Can't find the compagny
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetCompagnyPayload(
                        -1, "", 0,0
                )));
                account.setCompanyId(-1);
                account.save();
                UIStateManager.sendNotification(player, ToastTypeEnum.ERROR, "Votre entreprise n'existe plus");
                return;
            }
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new SetCompagnyPayload(
                    company.getId(), company.getName(), company.isOwner(player) ? 1:0, company.getBankCash()
            )));

            // Refresh employees
            player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new ClearCompagnyEmployeesPayload()));
            for(Player employee : getOnlineEmployees(company)) {
                Account eAccount = WorldManager.getPlayerAccount(employee);
                player.callRemoteEvent("GlobalUI:DispatchToUI", new Gson().toJson(new AddCompagnyEmployeePayload(
                        employee.getSteamId(), eAccount.getCharacterName(), company.isOwner(employee) ? 1 : 0, true
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
        Company company = getCompanyById(account.getCompanyId());
        if(company == null) return;
        UIStateManager.sendNotification(player, ToastTypeEnum.SUCCESS, "Invitation envoyée");
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(invitedPlayer);
        characterState.setCurrentCompagnyInvited(company.getId());

        // Build generic menu
        GenericMenu genericMenu = new GenericMenu(invitedPlayer);
        genericMenu.getItems().add(new GenericMenuItem("Voulez-vous rejoindre " + company.getName() + " ?", ""));
        genericMenu.getItems().add(new GenericMenuItem("Accepter", "window.CallEvent(\"RemoteCallInterface\", \"Compagny:AcceptInvitation\");"));
        genericMenu.getItems().add(new GenericMenuItem("Refuser", "window.CallEvent(\"RemoteCallInterface\", \"Compagny:DeclineInvitation\");"));
        genericMenu.show();
        characterState.setCurrentGenericMenu(genericMenu);
    }

    public static void handleAcceptInvitation(Player player) {
        Account account = WorldManager.getPlayerAccount(player);
        CharacterState characterState = CharacterManager.getCharacterStateByPlayer(player);
        Company company = getCompanyById(characterState.getCurrentCompagnyInvited());
        account.setCompanyId(company.getId());
        WorldManager.savePlayer(player);

        // Reset invitation
        characterState.setCurrentCompagnyInvited(-1);
        if(characterState.getCurrentGenericMenu() != null) {
            characterState.getCurrentGenericMenu().hide();
            characterState.setCurrentGenericMenu(null);
        }

        for (Player employee : getOnlineEmployees(company)) {
            refreshCompany(employee);
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
        Company company = getCompanyById(account.getCompanyId());
        if(!company.isOwner(player)) return;
        Player targetPlayer = WorldManager.findPlayerBySteamId(steamid);
        if(targetPlayer == null) return;
        Account targetAccount = WorldManager.getPlayerAccount(targetPlayer);
        if(targetAccount.getId() == account.getId() && company.isOwner(player)) {
            UIStateManager.sendNotification(targetPlayer, ToastTypeEnum.ERROR, "Action impossible, contactez un administrateur pour supprimer l'entreprise");
            return;
        }
        targetAccount.setCompanyId(-1);
        WorldManager.savePlayer(targetPlayer);

        for (Player employee : getOnlineEmployees(company)) {
            refreshCompany(employee);
            UIStateManager.sendNotification(employee, ToastTypeEnum.WARN, targetAccount.getCharacterName() + " fait désormais plus partie de l'entreprise");
        }
        UIStateManager.sendNotification(targetPlayer, ToastTypeEnum.ERROR, "Vous avez été licencier de l'entreprise " + company.getName());
        refreshCompany(targetPlayer);
    }
}
