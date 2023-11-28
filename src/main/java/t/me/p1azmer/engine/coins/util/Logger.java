package t.me.p1azmer.engine.coins.util;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.coins.api.CoinsEngineAPI;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.coins.config.Config;
import t.me.p1azmer.engine.coins.data.impl.CoinsUser;
import t.me.p1azmer.engine.utils.NumberUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {

    public static void logGive(@NotNull CoinsUser user, @NotNull Currency currency, double amount, @NotNull CommandSender from) {
        String text = user.getName() + " received " + currency.formatValue(amount) + " " + currency.getName()
            + " from " + from.getName() + ". New balance: " + currency.format(user.getCurrencyData(currency).getBalance());
        log(text);
    }

    public static void logSet(@NotNull CoinsUser user, @NotNull Currency currency, double amount, @NotNull CommandSender from) {
        String text = user.getName() + "'s " + currency.getName() + " balance set to " + currency.formatValue(amount)
            + " by " + from.getName() + ". New balance: " + currency.format(user.getCurrencyData(currency).getBalance());
        log(text);
    }

    public static void logTake(@NotNull CoinsUser user, @NotNull Currency currency, double amount, @NotNull CommandSender from) {
        String text = user.getName() + " lost " + currency.formatValue(amount) + " " + currency.getName()
            + " by " + from.getName() + ". New balance: " + currency.format(user.getCurrencyData(currency).getBalance());
        log(text);
    }

    public static void logExchange(@NotNull CoinsUser user, @NotNull Currency from, @NotNull Currency to, double amount, double result) {
        String text = user.getName() + " exchanged x" + NumberUtil.format(amount) + " " + from.getName()
            + " to x" + NumberUtil.format(result) + " " + to.getName()
            + ". New balance: " + from.format(user.getCurrencyData(from).getBalance()) + " and " + to.format(user.getCurrencyData(to).getBalance());
        log(text);
    }

    private static void log(@NotNull String text) {
        if (!Config.LOGS_TO_CONSOLE.get() && !Config.LOGS_TO_FILE.get()) return;

        if (Config.LOGS_TO_CONSOLE.get()) {
            CoinsEngineAPI.PLUGIN.info(text);
        }
        if (Config.LOGS_TO_FILE.get()) {
            String date = LocalDateTime.now().format(Config.LOGS_DATE_FORMAT.get());
            String path = CoinsEngineAPI.PLUGIN.getDataFolder() + "/" + Config.LOG_FILENAME;
            BufferedWriter output;
            try {
                output = new BufferedWriter(new FileWriter(path, true));
                output.append("[").append(date).append("] ").append(text);
                output.newLine();
                output.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
