package t.me.p1azmer.engine.coins.command.currency;

import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.command.*;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.api.currency.Currency;

public abstract class CurrencySubCommand extends GeneralCommand<CoinsEngine> {

    protected final Currency currency;

    public CurrencySubCommand(@NotNull CoinsEngine plugin, @NotNull Currency currency, @NotNull String[] aliases, @NotNull Permission permission) {
        this(plugin, currency, aliases, permission.getName());
    }

    public CurrencySubCommand(@NotNull CoinsEngine plugin, @NotNull Currency currency, @NotNull String[] aliases, @NotNull String permission) {
        super(plugin, aliases, permission);
        this.currency = currency;
    }
}
