package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Table("tbl_job_level")
public class JobLevel extends Model {
    @Column(column = "id_job_level")
    private int id;
    @Column(column = "id_job")
    private String jobId;
    @Column
    private String name;
    @Column
    private int level;
    @Column
    private int expFloor;

    public String getTranslateName() {
        return name.toLowerCase().replaceAll(" ", "_");
    }
}
