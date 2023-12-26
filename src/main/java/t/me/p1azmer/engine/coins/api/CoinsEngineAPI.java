package t.me.p1azmer.engine.coins.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.coins.currency.CurrencyManager;
import t.me.p1azmer.engine.coins.data.UserManager;
import t.me.p1azmer.engine.coins.data.impl.CoinsUser;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CoinsEngineAPI {

    public static final CoinsEngine PLUGIN = CoinsEngine.getPlugin(CoinsEngine.class);

    @NotNull
    public static UserManager getUserManager() {
        return PLUGIN.getUserManager();
    }

    @NotNull
    public static CurrencyManager getCurrencyManager() {
        return PLUGIN.getCurrencyManager();
    }

    public static double getBalance(@NotNull Player player, @NotNull Currency currency) {
        return getUserData(player).getCurrencyData(currency).getBalance();
    }

    public static void addBalance(@NotNull Player player, @NotNull Currency currency, double amount) {
        CoinsUser user = getUserData(player);
        user.getCurrencyData(currency).addBalance(amount);
        getUserManager().saveUser(user);
    }

    public static void setBalance(@NotNull Player player, @NotNull Currency currency, double amount) {
        CoinsUser user = getUserData(player);
        user.getCurrencyData(currency).setBalance(amount);
        getUserManager().saveUser(user);
    }

    public static void removeBalance(@NotNull Player player, @NotNull Currency currency, double amount) {
        CoinsUser user = getUserData(player);
        user.getCurrencyData(currency).removeBalance(amount);
        getUserManager().saveUser(user);
    }

    @NotNull
    public static CoinsUser getUserData(@NotNull Player player) {
        return getUserManager().getUserData(player);
    }

    @Nullable
    public static CoinsUser getUserData(@NotNull String name) {
        return getUserManager().getUserData(name);
    }

    @Nullable
    public static CoinsUser getUserData(@NotNull UUID uuid) {
        return getUserManager().getUserData(uuid);
    }

    @NotNull
    public static CompletableFuture<CoinsUser> getUserDataAsync(@NotNull UUID uuid) {
        return getUserManager().getUserDataAsync(uuid);
    }
    @NotNull
    public static CompletableFuture<CoinsUser> getUserDataAsync(@NotNull String name) {
        return getUserManager().getUserDataAsync(name);
    }

    @Nullable
    public static Currency getCurrency(@NotNull String id) {
        return getCurrencyManager().getCurrency(id);
    }

    public static boolean hasCurrency(@NotNull String id) {
        return getCurrency(id) != null;
    }

    public static void registerCurrency(@NotNull Currency currency) {
        getCurrencyManager().registerCurrency(currency);
    }
}
