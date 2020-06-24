package fr.yuki.YukiRPFramework.model;

import net.onfirenetwork.onsetjava.entity.Player;

public class Compagny {
    private int id;
    private String name;
    private int bankCash;
    private String owner;
    private int maxMember;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBankCash() {
        return bankCash;
    }

    public void setBankCash(int bankCash) {
        this.bankCash = bankCash;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getMaxMember() {
        return maxMember;
    }

    public void setMaxMember(int maxMember) {
        this.maxMember = maxMember;
    }

    public boolean isOwner(Player player) {
        return this.owner.equals(player.getSteamId());
    }
}
