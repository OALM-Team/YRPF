package fr.yuki.yrpf.dao;

import fr.yuki.yrpf.Database;
import fr.yuki.yrpf.model.Seller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SellerDAO {
    public static ArrayList<Seller> loadSellers() throws SQLException {
        ArrayList<Seller> sellers = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_seller");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Seller seller = new Seller();

            seller.setId(resultSet.getInt("id_seller"));
            seller.setName(resultSet.getString("name"));
            seller.setX(resultSet.getDouble("x"));
            seller.setY(resultSet.getDouble("y"));
            seller.setZ(resultSet.getDouble("z"));
            seller.setH(resultSet.getDouble("h"));
            seller.setNpcClothing(resultSet.getInt("npc_clothing"));
            seller.setItemList(resultSet.getString("item_list"));
            seller.setJobRequired(resultSet.getString("job_required"));
            seller.setJobLevelRequired(resultSet.getInt("job_level_required"));

            sellers.add(seller);
        }
        preparedStatement.close();
        resultSet.close();
        return sellers;
    }
}
