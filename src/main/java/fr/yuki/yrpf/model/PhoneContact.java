package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Table("tbl_phone_contact")
public class PhoneContact extends Model {
    @Column(column = "id_phone_contact")
    private int id;
    @Column(column = "id_account")
    private int accountId;
    @Column
    private String name;
    @Column(column = "phone_number")
    private String number;
}
