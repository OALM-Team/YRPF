package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Table("tbl_growbox")
public class GrowboxModel extends Model {
    @Column(column = "id_growbox")
    private int id;
    @Column
    private double x;
    @Column
    private double y;
    @Column
    private double z;
    @Column
    private double rx;
    @Column
    private double ry;
    @Column
    private double rz;
}
