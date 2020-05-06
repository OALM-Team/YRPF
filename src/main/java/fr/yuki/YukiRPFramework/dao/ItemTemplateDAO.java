package fr.yuki.YukiRPFramework.dao;

import fr.yuki.YukiRPFramework.Database;
import fr.yuki.YukiRPFramework.model.ItemTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ItemTemplateDAO {
    public static HashMap<Integer, ItemTemplate> getItemTemplates() throws SQLException {
        HashMap<Integer, ItemTemplate> itemTemplateHashMap = new HashMap<Integer, ItemTemplate>();
        PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM tbl_item_template");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            ItemTemplate itemTemplate = new ItemTemplate();
            itemTemplate.setId(resultSet.getInt("id_item_template"));
            itemTemplate.setName(resultSet.getString("name"));
            itemTemplate.setDescription(resultSet.getString("description"));
            itemTemplate.setWeight(resultSet.getFloat("weight"));
            itemTemplate.setPictureName(resultSet.getString("picture_name"));
            itemTemplate.setItemType(resultSet.getInt("item_type"));
            itemTemplateHashMap.put(itemTemplate.getId(), itemTemplate);
        }
        return itemTemplateHashMap;
    }
}
