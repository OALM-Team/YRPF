package fr.yuki.YukiRPFramework.net.payload;

public class ClearCompagnyEmployeesPayload {
    private String type;

    public ClearCompagnyEmployeesPayload() {
        this.type = "CLEAR_COMPAGNY_EMPLOYEES";
    }

    public String getType() {
        return type;
    }
}
