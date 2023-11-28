package t.me.p1azmer.engine.coins.command.currency.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.command.*;
import t.me.p1azmer.engine.lang.LangManager;
import t.me.p1azmer.engine.utils.*;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.Placeholders;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.coins.command.CommandFlags;
import t.me.p1azmer.engine.coins.command.currency.CurrencySubCommand;
import t.me.p1azmer.engine.coins.config.Lang;
import t.me.p1azmer.engine.coins.config.Perms;
import t.me.p1azmer.engine.coins.data.impl.CurrencyData;

import java.util.List;

public class PaymentsCommand extends CurrencySubCommand {

    public PaymentsCommand(@NotNull CoinsEngine plugin, @NotNull Currency currency) {
        super(plugin, currency, new String[]{"payments"}, Perms.COMMAND_CURRENCY_PAYMENTS);
        this.setDescription(plugin.getMessage(Lang.COMMAND_CURRENCY_PAYMENTS_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_CURRENCY_PAYMENTS_USAGE));
        this.addFlag(CommandFlags.SILENT);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1 && player.hasPermission(Perms.COMMAND_CURRENCY_PAYMENTS_OTHERS)) {
            return CollectionsUtil.playerNames(player);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() >= 2 && !sender.hasPermission(Perms.COMMAND_CURRENCY_PAYMENTS_OTHERS)) {
            this.errorPermission(sender);
            return;
        }
        if (result.length() <= 1 && !(sender instanceof Player)) {
            this.printUsage(sender);
            return;
        }

        String pName = result.getArg(1, sender.getName());
        this.plugin.getUserManager().getUserDataAsync(pName).thenAccept(user -> {
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            CurrencyData data = user.getCurrencyData(this.currency);
            data.setPaymentsEnabled(!data.isPaymentsEnabled());
            this.plugin.getUserManager().saveUser(user);

            if (!user.getName().equalsIgnoreCase(sender.getName())) {
                plugin.getMessage(Lang.COMMAND_CURRENCY_PAYMENTS_TARGET)
                    .replace(currency.replacePlaceholders())
                    .replace(Placeholders.PLAYER_NAME, user.getName())
                    .replace(Placeholders.GENERIC_STATE, LangManager.getBoolean(data.isPaymentsEnabled()))
                    .send(sender);
            }

            Player target = user.getPlayer();
            if (!result.hasFlag(CommandFlags.SILENT) && target != null) {
                plugin.getMessage(Lang.COMMAND_CURRENCY_PAYMENTS_TOGGLE)
                    .replace(currency.replacePlaceholders())
                    .replace(Placeholders.GENERIC_STATE, LangManager.getBoolean(data.isPaymentsEnabled()))
                    .send(target);
            }
        });
    }
}
