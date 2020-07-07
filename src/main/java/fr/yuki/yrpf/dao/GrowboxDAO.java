package fr.yuki.yrpf.dao;

import fr.yuki.yrpf.Database;
import fr.yuki.yrpf.model.GrowboxModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GrowboxDAO {
    public static ArrayList<GrowboxModel> loadGrowbox() throws SQLException {
        ArrayList<GrowboxModel> growboxs = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_growbox");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            GrowboxModel growboxModel = new GrowboxModel();
            growboxModel.setId(resultSet.getInt("id_growbox"));
            growboxModel.setX(resultSet.getFloat("x"));
            growboxModel.setY(resultSet.getFloat("y"));
            growboxModel.setZ(resultSet.getFloat("z"));
            growboxModel.setRx(resultSet.getFloat("rx"));
            growboxModel.setRy(resultSet.getFloat("ry"));
            growboxModel.setRz(resultSet.getFloat("rz"));
            growboxs.add(growboxModel);
        }
        return growboxs;
    }

    public static void insertGrowbox(GrowboxModel growboxModel) throws SQLException {
        // Execute the query
        PreparedStatement preparedStatement = Database.getConnection()
                .prepareStatement("INSERT INTO tbl_growbox " +
                        "(x, y, z, rx, ry, rz) VALUES " +
                        "(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setDouble(1, growboxModel.getX());
        preparedStatement.setDouble(2, growboxModel.getY());
        preparedStatement.setDouble(3, growboxModel.getZ());
        preparedStatement.setDouble(4, growboxModel.getRx());
        preparedStatement.setDouble(5, growboxModel.getRy());
        preparedStatement.setDouble(6, growboxModel.getRz());
        preparedStatement.executeUpdate();

        ResultSet returnId = preparedStatement.getGeneratedKeys();
        if(returnId.next()) {
            growboxModel.setId(returnId.getInt(1));
        }
        returnId.close();
    }

    public static void deleteGrowbox(GrowboxModel growboxModel) throws SQLException {
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("DELETE FROM tbl_growbox WHERE id_growbox=?");
        preparedStatement.setInt(1, growboxModel.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
