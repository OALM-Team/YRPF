package fr.yuki.yrpf.manager;

import eu.bebendorf.ajorm.Repo;
import fr.yuki.yrpf.model.AccountJobWhitelist;
import lombok.Getter;
import net.onfirenetwork.onsetjava.Onset;

import java.sql.SQLException;
import java.util.List;

public class AccountManager {
    @Getter
    private static List<AccountJobWhitelist> accountJobWhitelists;

    public static void init() throws SQLException {
        accountJobWhitelists = Repo.get(AccountJobWhitelist.class).all();
        Onset.print("Loaded " + accountJobWhitelists.size() + " account job whitelist(s) from database");
    }
}
