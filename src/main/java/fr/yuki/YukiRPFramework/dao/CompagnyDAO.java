package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.Compagny;
import fr.yuki.YukiRPFramework.model.PhoneContact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CompagnyDAO {
    public static ArrayList<Compagny> loadCompagnies() throws SQLException {
        ArrayList<Compagny> compagnies = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_compagny");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Compagny compagny = new Compagny();
            compagny.setId(resultSet.getInt("id_compagny"));
            compagny.setName(resultSet.getString("name"));
            compagny.setBankCash(resultSet.getInt("bank_cash"));
            compagny.setOwner(resultSet.getString("owner"));
            compagny.setMaxMember(resultSet.getInt("max_member"));

            compagnies.add(compagny);
        }
        return compagnies;
    }

    public static void insertCompagny(Compagny compagny) throws SQLException {
        // Execute the query
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("INSERT INTO tbl_compagny " +
                        "(name, bank_cash, owner, max_member) VALUES " +
                        "(?,?,?, ?)", Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, compagny.getName());
        preparedStatement.setInt(2, compagny.getBankCash());
        preparedStatement.setString(3, compagny.getOwner());
        preparedStatement.setInt(4, compagny.getMaxMember());
        preparedStatement.executeUpdate();

        ResultSet returnId = preparedStatement.getGeneratedKeys();
        if(returnId.next()) {
            compagny.setId(returnId.getInt(1));
        }
    }
}
