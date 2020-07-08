package fr.yuki.yrpf.net.payload;

public class AddClientContactPayload {
    private String type;

    private int id;
    private String name;
    private String number;

    public AddClientContactPayload(int id, String name, String number) {
        this.type = "ADD_CONTACT";
        this.id = id;
        this.name = name;
        this.number = number;
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

    public String getNumber() {
        return number;
    }
}
