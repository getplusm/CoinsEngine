package t.me.p1azmer.engine.coins.data.impl;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.coins.api.currency.Currency;

public class CurrencyData {

    private final Currency currency;

    private double balance;
    private boolean paymentsEnabled;

    public CurrencyData(@NotNull Currency currency, double balance, boolean paymentsEnabled) {
        this.currency = currency;
        this.setBalance(balance);
        this.setPaymentsEnabled(paymentsEnabled);
    }

    @NotNull
    public static CurrencyData create(@NotNull Currency currency) {
        return new CurrencyData(currency, currency.getStartValue(), true);
    }

    @NotNull
    public Currency getCurrency() {
        return currency;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double amount) {
        this.balance = this.getCurrency().fineAndLimit(amount);
    }

    public void addBalance(double amount) {
        this.setBalance(this.getBalance() + Math.abs(amount));
    }

    public void removeBalance(double amount) {
        this.setBalance(this.getBalance() - Math.abs(amount));
    }

    public boolean isPaymentsEnabled() {
        return paymentsEnabled;
    }

    public void setPaymentsEnabled(boolean paymentsEnabled) {
        this.paymentsEnabled = paymentsEnabled;
    }
}
