package t.me.p1azmer.engine.coins.command.currency.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.command.CommandResult;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.Placeholders;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.coins.command.currency.CurrencySubCommand;
import t.me.p1azmer.engine.coins.config.Lang;
import t.me.p1azmer.engine.coins.config.Perms;
import t.me.p1azmer.engine.coins.data.impl.CurrencyData;
import t.me.p1azmer.engine.utils.Colorizer;
import t.me.p1azmer.engine.utils.EngineUtils;
import t.me.p1azmer.plugin.gameui.GameUIAPI;

public class FreeCommand extends CurrencySubCommand {

    public FreeCommand(@NotNull CoinsEngine plugin, @NotNull Currency currency) {
        super(plugin, currency, new String[]{"free"}, Perms.COMMAND_CURRENCY_FREE);
        this.setDescription(plugin.getMessage(Lang.COMMAND_CURRENCY_FREE_DESC));
        this.setPlayerOnly(true);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (!(sender instanceof Player player)) {
            this.errorSender(sender);
            return;
        }

        if (!EngineUtils.hasPlugin("GameUI") || GameUIAPI.PLUGIN == null) {
            sender.sendMessage(Colorizer.apply("&cError when running command. Wait and try again"));
            return;
        }

        this.plugin.getUserManager().getUserDataAsync(player.getName()).thenAccept(user -> {
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            if (GameUIAPI.PLUGIN.getUserManager().checkCommandCooldown(player, "/" + this.getLabelFull())) {
                player.sendMessage("cd\n"+this.getLabelFull());
                return;
            }

            CurrencyData data = user.getCurrencyData(this.currency);
            data.addBalance(this.currency.getFreeValue());
            this.plugin.getMessage(Lang.CURRENCY_CURRENCY_FREE_SUCCESS)
                    .replace(this.currency.replacePlaceholders())
                    .replace(Placeholders.PLAYER_NAME, user.getName())
                    .replace(Placeholders.GENERIC_AMOUNT, currency.format(this.currency.getFreeValue()))
                    .send(sender);
        }).exceptionally(throwable -> {
            sender.sendMessage(throwable.getMessage());
            return null;
        });
    }
}
