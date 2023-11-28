package t.me.p1azmer.engine.coins.hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.coins.currency.impl.CurrencyEconomy;

public class VaultEconomyHook {

    private static CurrencyEconomy economy;

    public static void setup(@NotNull CoinsEngine plugin, @NotNull Currency currency) {
        economy = new CurrencyEconomy(plugin, currency);

        ServicesManager services = plugin.getServer().getServicesManager();
        services.register(Economy.class, economy, plugin, ServicePriority.High);

        plugin.info("Registered '" + currency.getId() + "' as Vault Economy!");
    }

    public static void shutdown() {
        if (economy != null) {
            ServicesManager services = Bukkit.getServer().getServicesManager();
            services.unregister(Economy.class, economy);
            economy = null;
        }
    }
}
