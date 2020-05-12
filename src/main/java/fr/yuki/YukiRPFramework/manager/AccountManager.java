package fr.yuki.YukiRPFramework.manager;

import fr.yuki.YukiRPFramework.dao.AccountJobWhitelistDAO;
import fr.yuki.YukiRPFramework.model.AccountJobWhitelist;
import net.onfirenetwork.onsetjava.Onset;

import java.sql.SQLException;
import java.util.ArrayList;

public class AccountManager {
    private static ArrayList<AccountJobWhitelist> accountJobWhitelists;

    public static void init() throws SQLException {
        accountJobWhitelists = AccountJobWhitelistDAO.loadAccountJobWhitelist();
        Onset.print("Loaded " + accountJobWhitelists.size() + " account job whitelist(s) from database");
    }

    public static ArrayList<AccountJobWhitelist> getAccountJobWhitelists() {
        return accountJobWhitelists;
    }
}
