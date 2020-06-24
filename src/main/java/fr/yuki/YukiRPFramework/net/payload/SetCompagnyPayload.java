package fr.yuki.YukiRPFramework.net.payload;

public class SetCompagnyPayload {
    private String type;
    private int id;
    private String name;
    private int myrank;
    private int bankCash;

    public SetCompagnyPayload(int id, String name, int myrank, int bankCash) {
        this.type = "SET_COMPAGNY";
        this.id = id;
        this.name = name;
        this.myrank = myrank;
        this.bankCash = bankCash;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMyrank() {
        return myrank;
    }

    public int getBankCash() {
        return bankCash;
    }
}
