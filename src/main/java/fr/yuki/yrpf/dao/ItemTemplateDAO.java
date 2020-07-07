package fr.yuki.yrpf.dao;

import fr.yuki.yrpf.Database;
import fr.yuki.yrpf.model.ItemTemplate;

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
            itemTemplate.setModelId(resultSet.getInt("model_id"));
            itemTemplate.setModelScale(resultSet.getDouble("model_scale"));
            itemTemplate.setFoodValue(resultSet.getInt("food_value"));
            itemTemplate.setDrinkValue(resultSet.getInt("drink_value"));
            itemTemplate.setWeaponId(resultSet.getInt("id_weapon"));
            itemTemplate.setAmmoPerRecharge(resultSet.getInt("ammo_per_recharge"));
            itemTemplate.setMaskId(resultSet.getInt("id_mask"));
            itemTemplate.setBagId(resultSet.getInt("id_bag"));
            itemTemplateHashMap.put(itemTemplate.getId(), itemTemplate);
        }
        return itemTemplateHashMap;
    }
}
