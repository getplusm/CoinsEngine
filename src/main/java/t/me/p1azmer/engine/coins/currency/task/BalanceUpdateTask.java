package t.me.p1azmer.engine.coins.currency.task;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.coins.config.Config;
import t.me.p1azmer.engine.api.server.AbstractTask;
import t.me.p1azmer.engine.utils.CollectionsUtil;
import t.me.p1azmer.engine.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BalanceUpdateTask extends AbstractTask<CoinsEngine> {

    public BalanceUpdateTask(@NotNull CoinsEngine plugin) {
        super(plugin, Config.TOP_UPDATE_INTERVAL.get(), true);
    }

    @Override
    public void action() {
        Map<Currency, List<Pair<String, Double>>> balanceMap = this.plugin.getCurrencyManager().getBalanceMap();
        Map<Currency, Map<String, Double>> dataMap = this.plugin.getData().getBalances();

        balanceMap.clear();
        dataMap.forEach((currency, users) -> {
            CollectionsUtil.sortDescent(users).forEach((name, balance) -> {
                balanceMap.computeIfAbsent(currency, k -> new ArrayList<>()).add(Pair.of(name, balance));
            });
        });
        //plugin.info("Balance top updated!");
    }
}
