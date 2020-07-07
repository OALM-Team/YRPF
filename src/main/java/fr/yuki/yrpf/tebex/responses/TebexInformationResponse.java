package fr.yuki.yrpf.tebex.responses;

public class TebexInformationResponse {
    private Account account;
    private Server server;

    public Account getAccount() { return account; }
    public void setAccount(Account value) { this.account = value; }

    public Server getServer() { return server; }
    public void setServer(Server value) { this.server = value; }
}
