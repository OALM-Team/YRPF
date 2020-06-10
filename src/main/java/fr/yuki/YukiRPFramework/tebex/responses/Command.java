package fr.yuki.YukiRPFramework.tebex.responses;

public class Command {
    private long id;
    private String command;
    private long payment;
    private long commandPackage;
    private Conditions conditions;
    private TebexPlayer player;

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getCommand() { return command; }
    public void setCommand(String value) { this.command = value; }

    public long getPayment() { return payment; }
    public void setPayment(long value) { this.payment = value; }

    public long getCommandPackage() { return commandPackage; }
    public void setCommandPackage(long value) { this.commandPackage = value; }

    public Conditions getConditions() { return conditions; }
    public void setConditions(Conditions value) { this.conditions = value; }

    public TebexPlayer getPlayer() { return player; }
    public void setPlayer(TebexPlayer value) { this.player = value; }
}