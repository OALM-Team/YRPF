package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.inventory.Inventory;
import fr.yuki.YukiRPFramework.model.ATM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ATMDAO {
    public static ArrayList<ATM> loadATMs() throws SQLException {
        ArrayList<ATM> atms = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_atm");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            ATM atm = new ATM();
            atm.setId(resultSet.getInt("id_atm"));
            atm.setX(resultSet.getFloat("x"));
            atm.setY(resultSet.getFloat("y"));
            atm.setZ(resultSet.getFloat("z"));
            atms.add(atm);
        }
        return atms;
    }
}
