package fr.yuki.YukiRPFramework.net.payload;

public class SetBankCashAmount {
    private String type;
    private int amount;
    private int cashAmount;

    public SetBankCashAmount(int amount, int cashAmount) {
        this.type = "SET_BANK_CASH_AMOUNT";
        this.amount = amount;
        this.cashAmount = cashAmount;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public int getCashAmount() {
        return cashAmount;
    }
}
