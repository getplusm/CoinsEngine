package t.me.p1azmer.engine.coins.command.currency.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.command.*;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.coins.command.currency.CurrencySubCommand;
import t.me.p1azmer.engine.coins.util.CoinsUtils;
import t.me.p1azmer.engine.coins.util.Logger;
import t.me.p1azmer.engine.utils.*;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.Placeholders;
import t.me.p1azmer.engine.coins.command.CommandFlags;
import t.me.p1azmer.engine.coins.config.Lang;
import t.me.p1azmer.engine.coins.config.Perms;
import t.me.p1azmer.engine.coins.data.impl.CurrencyData;

import java.util.Arrays;
import java.util.List;

public class TakeCommand extends CurrencySubCommand {

    public TakeCommand(@NotNull CoinsEngine plugin, @NotNull Currency currency) {
        super(plugin, currency, new String[]{"take"}, Perms.COMMAND_CURRENCY_TAKE);
        this.setDescription(plugin.getMessage(Lang.COMMAND_CURRENCY_TAKE_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_CURRENCY_TAKE_USAGE));
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
        if (amount <= 0D) return;

        this.plugin.getUserManager().getUserDataAndPerform(result.getArg(1), user -> {
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            //this.plugin.runTask(task -> {
                CurrencyData data = user.getCurrencyData(this.currency);
                data.removeBalance(amount);

                if (!result.hasFlag(CommandFlags.NO_SAVE)) {
                    this.plugin.getUserManager().saveUser(user);
                }

                Logger.logTake(user, currency, amount, sender);

                plugin.getMessage(Lang.COMMAND_CURRENCY_TAKE_DONE)
                    .replace(currency.replacePlaceholders())
                    .replace(Placeholders.PLAYER_NAME, user.getName())
                    .replace(Placeholders.GENERIC_AMOUNT, currency.formatValue(amount))
                    .replace(Placeholders.GENERIC_BALANCE, currency.format(data.getBalance()))
                    .send(sender);

                Player target = user.getPlayer();
                if (!result.hasFlag(CommandFlags.SILENT) && target != null) {
                    plugin.getMessage(Lang.COMMAND_CURRENCY_TAKE_NOTIFY)
                        .replace(currency.replacePlaceholders())
                        .replace(Placeholders.GENERIC_AMOUNT, currency.format(amount))
                        .replace(Placeholders.GENERIC_BALANCE, currency.format(data.getBalance()))
                        .send(target);
                }
            //});
        });
    }
}
