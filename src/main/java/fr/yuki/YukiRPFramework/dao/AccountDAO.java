package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.Account;
import net.onfirenetwork.onsetjava.Onset;
import net.onfirenetwork.onsetjava.data.Location;
import net.onfirenetwork.onsetjava.entity.Player;
import net.onfirenetwork.onsetjava.enums.Animation;

import java.sql.*;

public class AccountDAO {
    /**
     * Find the account for a player steam id
     * @param steamId The steam id
     * @return The account
     */
    public static Account findAccountBySteamId(String steamId) throws SQLException {
        Account account = null;
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("SELECT * FROM tbl_account WHERE steam_id=?");
        preparedStatement.setString(1, steamId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            account = new Account();
            account.setId(resultSet.getInt("id_account"));
            account.setSteamName(resultSet.getString("steam_account_name"));
            account.setSteamId(resultSet.getString("steam_id"));
            account.setIsBanned(resultSet.getInt("is_banned"));
            account.setBankMoney(resultSet.getInt("bank_money"));
            account.setSaveX(resultSet.getDouble("save_x"));
            account.setSaveY(resultSet.getDouble("save_y"));
            account.setSaveZ(resultSet.getDouble("save_z"));
            account.setSaveH(resultSet.getDouble("save_h"));
            account.setCharacterCreationRequest(resultSet.getInt("character_creation_request"));
            account.setCharacterStyle(resultSet.getString("character_style"));
            account.setCharacterName(resultSet.getString("character_name"));
            account.setJobLevels(resultSet.getString("job_levels"));
            account.setCreatedAt(resultSet.getDate("created_at"));
            account.setUpdatedAt(resultSet.getDate("updated_at"));
        }
        return account;
    }

    public static Account createAccount(Player player) throws SQLException {
        // Create the account object
        Account account = new Account();
        account.setSteamName(player.getName());
        account.setSteamId(player.getSteamId());
        account.setIsBanned(0);
        account.setCharacterCreationRequest(1);
        account.setCharacterStyle("");
        account.setCharacterName("Unknown");
        account.setJobLevels("[]");
        account.setCreatedAt(new java.util.Date());
        account.setUpdatedAt(new java.util.Date());

        // Execute the query
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("INSERT INTO tbl_account " +
                        "(steam_account_name, steam_id, is_banned, created_at, updated_at, character_creation_request, character_style, character_name) VALUES " +
                        "(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, account.getSteamName());
        preparedStatement.setString(2, account.getSteamId());
        preparedStatement.setInt(3, account.getIsBanned());
        preparedStatement.setDate(4, new Date(account.getCreatedAt().getTime()));
        preparedStatement.setDate(5, new Date(account.getUpdatedAt().getTime()));
        preparedStatement.setInt(6, account.getCharacterCreationRequest());
        preparedStatement.setString(7, account.getCharacterStyle());
        preparedStatement.setString(8, account.getCharacterName());
        preparedStatement.executeUpdate();

        ResultSet returnId = preparedStatement.getGeneratedKeys();
        if(returnId.next()) {
            account.setId(returnId.getInt(1));
        } else {
            return null;
        }
        return account;
    }

    public static void updateAccount(Account account, Player player) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("UPDATE tbl_account SET is_banned=?, bank_money=?, save_x=?, save_y=?, save_z=?, save_h=?, character_creation_request=?," +
                        "character_style=?, character_name=?, job_levels=? WHERE id_account=?");
        Location loc = player.getLocationAndHeading();
        preparedStatement.setInt(1, account.getIsBanned());
        preparedStatement.setInt(2, account.getBankMoney());
        preparedStatement.setDouble(3, loc.getX());
        preparedStatement.setDouble(4, loc.getY());
        preparedStatement.setDouble(5, loc.getZ());
        preparedStatement.setDouble(6, loc.getHeading());
        preparedStatement.setInt(7, account.getCharacterCreationRequest());
        preparedStatement.setString(8, account.getCharacterStyle());
        preparedStatement.setString(9, account.getCharacterName());
        preparedStatement.setString(10, account.getJobLevels());
        preparedStatement.setDouble(11, account.getId());
        preparedStatement.execute();
    }
}
