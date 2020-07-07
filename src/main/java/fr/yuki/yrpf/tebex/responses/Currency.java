package fr.yuki.yrpf.tebex.responses;

public class Currency {
    private String iso4217;
    private String symbol;

    public String getIso4217() { return iso4217; }
    public void setIso4217(String value) { this.iso4217 = value; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String value) { this.symbol = value; }
}