package t.me.p1azmer.engine.coins.command.base;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.command.AbstractCommand;
import t.me.p1azmer.engine.api.command.CommandResult;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.Placeholders;
import t.me.p1azmer.engine.coins.config.Lang;
import t.me.p1azmer.engine.coins.config.Perms;
import t.me.p1azmer.engine.utils.CollectionsUtil;

import java.util.List;

public class ResetCommand extends AbstractCommand<CoinsEngine> {

    public ResetCommand(@NotNull CoinsEngine plugin) {
        super(plugin, new String[]{"reset"}, Perms.COMMAND_RESET);
        this.setDescription(plugin.getMessage(Lang.COMMAND_RESET_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_RESET_USAGE));
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return CollectionsUtil.playerNames(player);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 2) {
            this.printUsage(sender);
            return;
        }

        this.plugin.getUserManager().getUserDataAsync(result.getArg(1)).thenAccept(user -> {
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            user.getCurrencyDataMap().clear();

            this.plugin.getUserManager().saveUser(user);
            this.plugin.getMessage(Lang.COMMAND_RESET_DONE)
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .send(sender);
        });
    }
}
