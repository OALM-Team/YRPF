package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.AccountJobWhitelist;
import fr.yuki.YukiRPFramework.model.FuelPoint;
import fr.yuki.YukiRPFramework.model.PhoneContact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PhoneContactDAO {
    public static ArrayList<PhoneContact> loadPhoneContacts() throws SQLException {
        ArrayList<PhoneContact> phoneContacts = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_phone_contact");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            PhoneContact phoneContact = new PhoneContact();
            phoneContact.setId(resultSet.getInt("id_phone_contact"));
            phoneContact.setAccountId(resultSet.getInt("id_account"));
            phoneContact.setName(resultSet.getString("name"));
            phoneContact.setNumber(resultSet.getString("phone_number"));

            phoneContacts.add(phoneContact);
        }
        return phoneContacts;
    }

    public static void insertPhoneContact(PhoneContact phoneContact) throws SQLException {
        // Execute the query
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("INSERT INTO tbl_phone_contact " +
                        "(id_account, name, phone_number) VALUES " +
                        "(?,?,?)", Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setInt(1, phoneContact.getAccountId());
        preparedStatement.setString(2, phoneContact.getName());
        preparedStatement.setString(3, phoneContact.getNumber());
        preparedStatement.executeUpdate();

        ResultSet returnId = preparedStatement.getGeneratedKeys();
        if(returnId.next()) {
            phoneContact.setId(returnId.getInt(1));
        }
    }
}
