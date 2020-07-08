package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Table("tbl_item_template")
public class ItemTemplate extends Model {
    @Column(column = "id_item_template")
    private int id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private float weight;
    @Column
    private String pictureName;
    @Column
    private int itemType;
    @Column
    private int modelId;
    @Column
    private double modelScale;
    @Column
    private int foodValue;
    @Column
    private int drinkValue;
    @Column(column = "id_weapon")
    private int weaponId;
    @Column
    private int ammoPerRecharge;
    @Column(column = "id_mask")
    private int maskId;
    @Column(column = "id_bag")
    private int bagId;
}
