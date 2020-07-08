package fr.yuki.yrpf.model;

import eu.bebendorf.ajorm.Model;
import eu.bebendorf.ajorm.annotation.Column;
import eu.bebendorf.ajorm.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import net.onfirenetwork.onsetjava.entity.Player;

@Getter @Setter @Table("tbl_compagny")
public class Company extends Model {
    @Column(column = "id_compagny")
    private int id;
    @Column
    private String name;
    @Column
    private int bankCash = 0;
    @Column
    private String owner;
    @Column
    private int maxMember = 5;

    public boolean isOwner(Player player) {
        return this.owner.equals(player.getSteamId());
    }
}
