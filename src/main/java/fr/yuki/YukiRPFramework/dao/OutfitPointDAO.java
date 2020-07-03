package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.FuelPoint;
import fr.yuki.YukiRPFramework.model.OutfitPoint;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OutfitPointDAO {
    public static ArrayList<OutfitPoint> loadOutfitPoint() throws SQLException {
        ArrayList<OutfitPoint> outfitPoints = new ArrayList<>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_outfit_point");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            OutfitPoint outfitPoint = new OutfitPoint();
            outfitPoint.setId(resultSet.getInt("id_outfit_point"));
            outfitPoint.setX(resultSet.getDouble("x"));
            outfitPoint.setY(resultSet.getDouble("y"));
            outfitPoint.setZ(resultSet.getDouble("z"));

            outfitPoints.add(outfitPoint);
        }
        return outfitPoints;
    }
}
