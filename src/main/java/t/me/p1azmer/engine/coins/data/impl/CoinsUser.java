package t.me.p1azmer.engine.coins.data.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.api.data.AbstractUser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CoinsUser extends AbstractUser<CoinsEngine> {

    private final Map<String, CurrencyData> currencyDataMap;

    public CoinsUser(@NotNull CoinsEngine plugin, @NotNull UUID uuid, @NotNull String name) {
        this(plugin, uuid, name, System.currentTimeMillis(), System.currentTimeMillis(),
            new HashSet<>());
    }

    public CoinsUser(
            @NotNull CoinsEngine plugin,
            @NotNull UUID uuid,
            @NotNull String name,
            long dateCreated,
            long lastLogin,
            @NotNull Set<CurrencyData> currencyDatas
    ) {
        super(plugin, uuid, name, dateCreated, lastLogin);
        this.currencyDataMap = new ConcurrentHashMap<>();
        currencyDatas.forEach(data -> this.getCurrencyDataMap().put(data.getCurrency().getId(), data));
        this.plugin.getCurrencyManager().getCurrencies().forEach(this::getCurrencyData);
    }

    @NotNull
    public Map<String, CurrencyData> getCurrencyDataMap() {
        return currencyDataMap;
    }

    @NotNull
    public CurrencyData getCurrencyData(@NotNull Currency currency) {
        return this.getCurrencyDataMap().computeIfAbsent(currency.getId(), k -> CurrencyData.create(currency));
    }

    @Nullable
    public CurrencyData getCurrencyData(@NotNull String id) {
        return this.getCurrencyDataMap().get(id.toLowerCase());
    }

    public double getBalance(@NotNull Currency currency) {
        return this.getCurrencyData(currency).getBalance();
    }

    public void addBalance(@NotNull Currency currency, double amount) {
        this.getCurrencyData(currency).addBalance(amount);
    }

    public void setBalance(@NotNull Currency currency, double amount) {
        this.getCurrencyData(currency).setBalance(amount);
    }

    public void removeBalance(@NotNull Currency currency, double amount) {
        this.getCurrencyData(currency).removeBalance(amount);
    }
}
