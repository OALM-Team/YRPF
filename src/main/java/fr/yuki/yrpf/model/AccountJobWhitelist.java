package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Table("tbl_account_job_whitelist")
public class AccountJobWhitelist extends Model {
    @Column(column = "id_account_job_whitelist")
    private int id;
    @Column(column = "id_account")
    private int accountId;
    @Column(column = "id_job")
    private String jobId;
    @Column
    private int jobLevel;
}
