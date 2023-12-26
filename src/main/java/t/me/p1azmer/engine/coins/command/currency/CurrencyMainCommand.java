package t.me.p1azmer.engine.coins.command.currency;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.command.*;
import t.me.p1azmer.engine.coins.command.currency.impl.*;
import t.me.p1azmer.engine.command.list.HelpSubCommand;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.utils.EngineUtils;

public class CurrencyMainCommand extends GeneralCommand<CoinsEngine> {

    public CurrencyMainCommand(@NotNull CoinsEngine plugin, @NotNull Currency currency) {
        super(plugin, currency.getCommandAliases(), currency.isPermissionRequired() ? currency.getPermission() : null);

        this.addChildren(new HelpSubCommand<>(plugin));
        this.addDefaultCommand(new BalanceCommand(plugin, currency));
        this.addChildren(new TopCommand(plugin, currency, "top"));
        this.addChildren(new GiveCommand(plugin, currency));
        this.addChildren(new SetCommand(plugin, currency));
        this.addChildren(new TakeCommand(plugin, currency));
        if (currency.isTransferAllowed()) {
            this.addChildren(new SendCommand(plugin, currency));
            this.addChildren(new PaymentsCommand(plugin, currency));
        }
        if (currency.isExchangeAllowed()) {
            this.addChildren(new ExchangeCommand(plugin, currency));
        }
        if (EngineUtils.hasPlugin("GameUI") && currency.isFreeAllowed()){
            this.addChildren(new FreeCommand(plugin, currency));
        }
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {

    }
}
