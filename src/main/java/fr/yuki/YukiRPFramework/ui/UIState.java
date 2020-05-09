package fr.yuki.YukiRPFramework.ui;

public class UIState {
    private boolean inventory = false;
    private boolean atm = false;
    private boolean garage = false;
    private boolean vseller = false;
    private boolean customCharacter = false;
    private boolean vchest = false;
    private boolean bigmap = false;

    public boolean isInventory() {
        return inventory;
    }

    public void setInventory(boolean inventory) {
        this.inventory = inventory;
    }

    public boolean isAtm() {
        return atm;
    }

    public void setAtm(boolean atm) {
        this.atm = atm;
    }

    public boolean isGarage() {
        return garage;
    }

    public void setGarage(boolean garage) {
        this.garage = garage;
    }

    public boolean isVseller() {
        return vseller;
    }

    public void setVseller(boolean vseller) {
        this.vseller = vseller;
    }

    public boolean isCustomCharacter() {
        return customCharacter;
    }

    public void setCustomCharacter(boolean customCharacter) {
        this.customCharacter = customCharacter;
    }

    public boolean isVchest() {
        return vchest;
    }

    public void setVchest(boolean vchest) {
        this.vchest = vchest;
    }

    public boolean isBigmap() {
        return bigmap;
    }

    public void setBigmap(boolean bigmap) {
        this.bigmap = bigmap;
    }
}
