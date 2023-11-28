package t.me.p1azmer.engine.coins.command.currency.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.command.*;
import t.me.p1azmer.engine.coins.command.currency.CurrencySubCommand;
import t.me.p1azmer.engine.coins.util.CoinsUtils;
import t.me.p1azmer.engine.coins.util.Logger;
import t.me.p1azmer.engine.utils.*;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.Placeholders;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.coins.command.CommandFlags;
import t.me.p1azmer.engine.coins.config.Lang;
import t.me.p1azmer.engine.coins.config.Perms;
import t.me.p1azmer.engine.coins.data.impl.CurrencyData;

import java.util.Arrays;
import java.util.List;

public class GiveCommand extends CurrencySubCommand {

    public GiveCommand(@NotNull CoinsEngine plugin, @NotNull Currency currency) {
        super(plugin, currency, new String[]{"give"}, Perms.COMMAND_CURRENCY_ADD);
        this.setDescription(plugin.getMessage(Lang.COMMAND_CURRENCY_GIVE_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_CURRENCY_GIVE_USAGE));
        this.addFlag(CommandFlags.SILENT, CommandFlags.NO_SAVE);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return CollectionsUtil.playerNames(player);
        }
        if (arg == 2) {
            return Arrays.asList("1", "10", "50", "100");
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 3) {
            this.printUsage(sender);
            return;
        }

        double amount = CoinsUtils.getAmountFromInput(result.getArg(2));
        if (amount <= 0) return;

        this.plugin.getUserManager().getUserDataAsync(result.getArg(1)).thenAccept(user -> {
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            this.plugin.runTask(task -> {
                CurrencyData data = user.getCurrencyData(this.currency);
                data.addBalance(amount);

                if (!result.hasFlag(CommandFlags.NO_SAVE)) {
                    this.plugin.getUserManager().saveUser(user);
                }

                Logger.logGive(user, currency, amount, sender);

                plugin.getMessage(Lang.COMMAND_CURRENCY_GIVE_DONE)
                    .replace(currency.replacePlaceholders())
                    .replace(Placeholders.PLAYER_NAME, user.getName())
                    .replace(Placeholders.GENERIC_AMOUNT, currency.format(amount))
                    .replace(Placeholders.GENERIC_BALANCE, currency.format(data.getBalance()))
                    .send(sender);

                Player target = user.getPlayer();
                if (!result.hasFlag(CommandFlags.SILENT) && target != null) {
                    plugin.getMessage(Lang.COMMAND_CURRENCY_GIVE_NOTIFY)
                        .replace(currency.replacePlaceholders())
                        .replace(Placeholders.GENERIC_AMOUNT, currency.format(amount))
                        .replace(Placeholders.GENERIC_BALANCE, currency.format(data.getBalance()))
                        .send(target);
                }
            });
        });
    }
}
