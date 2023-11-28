package t.me.p1azmer.engine.coins.command.currency.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.command.*;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.coins.command.currency.CurrencySubCommand;
import t.me.p1azmer.engine.utils.*;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.Placeholders;
import t.me.p1azmer.engine.coins.config.Lang;
import t.me.p1azmer.engine.coins.config.Perms;
import t.me.p1azmer.engine.coins.data.impl.CurrencyData;

import java.util.List;

public class BalanceCommand extends CurrencySubCommand {

    public BalanceCommand(@NotNull CoinsEngine plugin, @NotNull Currency currency) {
        super(plugin, currency, new String[]{"balance", "bal"}, Perms.COMMAND_CURRENCY_BALANCE);
        this.setDescription(plugin.getMessage(Lang.COMMAND_CURRENCY_BALANCE_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_CURRENCY_BALANCE_USAGE));
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1 && player.hasPermission(Perms.COMMAND_CURRENCY_BALANCE_OTHERS)) {
            return CollectionsUtil.playerNames(player);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        int indexOff = this.getParent() == null ? 1 : 0;

        if ((result.length() < 2 - indexOff && !(sender instanceof Player))) {
            this.printUsage(sender);
            return;
        }

        if (result.length() >= 2 - indexOff && !sender.hasPermission(Perms.COMMAND_CURRENCY_BALANCE_OTHERS)) {
            this.errorPermission(sender);
            return;
        }

        this.plugin.getUserManager().getUserDataAsync(result.getArg(1 - indexOff, sender.getName())).thenAccept(user -> {
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            boolean isOwn = user.getName().equalsIgnoreCase(sender.getName());
            CurrencyData data = user.getCurrencyData(this.currency);
            this.plugin.getMessage(isOwn ? Lang.CURRENCY_BALANCE_DISPLAY_OWN : Lang.CURRENCY_BALANCE_DISPLAY_OTHERS)
                .replace(this.currency.replacePlaceholders())
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .replace(Placeholders.GENERIC_BALANCE, currency.format(data.getBalance()))
                .send(sender);
        });
    }
}
