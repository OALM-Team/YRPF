package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.ATM;
import fr.yuki.YukiRPFramework.model.AccountJobWhitelist;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountJobWhitelistDAO {
    public static ArrayList<AccountJobWhitelist> loadAccountJobWhitelist() throws SQLException {
        ArrayList<AccountJobWhitelist> accountJobWhitelists = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_account_job_whitelist");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            AccountJobWhitelist accountJobWhitelist = new AccountJobWhitelist();
            accountJobWhitelist.setId(resultSet.getInt("id_account_job_whitelist"));
            accountJobWhitelist.setAccountId(resultSet.getInt("id_account"));
            accountJobWhitelist.setJobId(resultSet.getString("id_job"));
            accountJobWhitelist.setJobLevel(resultSet.getInt("job_level"));
            accountJobWhitelists.add(accountJobWhitelist);
        }
        return accountJobWhitelists;
    }
}
