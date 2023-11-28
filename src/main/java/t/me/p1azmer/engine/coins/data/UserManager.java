package t.me.p1azmer.engine.coins.data;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.data.impl.CoinsUser;
import t.me.p1azmer.engine.api.data.AbstractUserManager;

import java.util.UUID;

public class UserManager extends AbstractUserManager<CoinsEngine, CoinsUser> {

    public UserManager(@NotNull CoinsEngine plugin) {
        super(plugin, plugin);
    }

    @Override
    @NotNull
    protected CoinsUser createData(@NotNull UUID uuid, @NotNull String name) {
        return new CoinsUser(plugin, uuid, name);
    }
}
