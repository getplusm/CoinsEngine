package t.me.p1azmer.engine.coins.currency.listener;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.currency.CurrencyManager;
import t.me.p1azmer.engine.api.manager.AbstractListener;

public class CurrencyListener extends AbstractListener<CoinsEngine> {

    //private final CurrencyManager manager;

    public CurrencyListener(@NotNull CurrencyManager manager) {
        super(manager.plugin());
        //this.manager = manager;
    }

}
