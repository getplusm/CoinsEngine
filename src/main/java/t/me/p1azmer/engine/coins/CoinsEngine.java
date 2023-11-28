package t.me.p1azmer.engine.coins;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.coins.command.base.ResetCommand;
import t.me.p1azmer.engine.coins.command.base.WipeCommand;
import t.me.p1azmer.engine.coins.config.Config;
import t.me.p1azmer.engine.coins.config.Lang;
import t.me.p1azmer.engine.coins.config.Perms;
import t.me.p1azmer.engine.coins.currency.CurrencyManager;
import t.me.p1azmer.engine.coins.data.DataHandler;
import t.me.p1azmer.engine.coins.data.UserManager;
import t.me.p1azmer.engine.coins.data.impl.CoinsUser;
import t.me.p1azmer.engine.coins.hook.PlaceholderAPIHook;
import t.me.p1azmer.engine.NexPlugin;
import t.me.p1azmer.engine.api.command.GeneralCommand;
import t.me.p1azmer.engine.api.data.UserDataHolder;
import t.me.p1azmer.engine.command.list.ReloadSubCommand;
import t.me.p1azmer.engine.utils.EngineUtils;

public class CoinsEngine extends NexPlugin<CoinsEngine> implements UserDataHolder<CoinsEngine, CoinsUser> {

    private CurrencyManager currencyManager;
    private DataHandler dataHandler;
    private UserManager userManager;

    @Override
    @NotNull
    protected CoinsEngine getSelf() {
        return this;
    }

    @Override
    public void enable() {
        this.currencyManager = new CurrencyManager(this);
        this.currencyManager.setup();

        /*this.getCurrencyManager().getVaultCurrency().ifPresent((currency) -> {
            int count = 0;
            for (JYML cfg : JYML.loadAll(this.getDataFolder() + "/data/")) {
                try {
                    UUID uuid = UUID.fromString(cfg.getString("UUID", ""));
                    double balance = cfg.getDouble("Balance");
                    if (!this.getData().isUserExists(uuid) && balance > 0D) {
                        OfflinePlayer offlinePlayer = this.getServer().getOfflinePlayer(uuid);
                        String name = offlinePlayer.getName();
                        if (name == null) name = "UnknownName" + (count++);

                        CoinsUser user = new CoinsUser(this, uuid, name);
                        user.getCurrencyData(currency).setBalance(balance);
                        this.getData().addUser(user);
                        this.info("Migrated user balance: '" + uuid + "' with " + balance);
                    }
                }
                catch (IllegalArgumentException ignored) {

                }
            }
        });

        try (BufferedReader reader = new BufferedReader(new FileReader(this.getDataFolder() + "/variables.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("shards") || line.startsWith("shinyshards")) {
                    String[] splitType = line.split("::");
                    String curId = splitType[0];

                    Currency currency = this.getCurrencyManager().getCurrency(curId);
                    if (currency == null) continue;

                    String[] data = splitType[1].split(",");
                    UUID userId = UUID.fromString(data[0].trim());
                    long amount = Long.parseLong(data[2].trim(), 16);
                    if (amount == 0) continue;

                    boolean created = true;
                    CoinsUser user = this.getData().getUser(userId);
                    if (user == null) {
                        user = new CoinsUser(this, userId, userId.toString());
                        created = false;
                    }

                    user.getCurrencyData(currency).setBalance(amount);
                    this.info("Migrated skript data: Currency: " + curId + ", User Id: " + userId + ", Amount: " + amount);

                    if (!created) {
                        this.getData().addUser(user);
                    }
                    else {
                        this.getData().saveUser(user);
                    }
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }*/

        if (EngineUtils.hasPlaceholderAPI()) {
            PlaceholderAPIHook.setup(this);
        }
    }

    @Override
    public void disable() {
        if (EngineUtils.hasPlaceholderAPI()) {
            PlaceholderAPIHook.shutdown();
        }
        if (this.currencyManager != null) {
            this.currencyManager.shutdown();
            this.currencyManager = null;
        }
    }

    @Override
    public boolean setupDataHandlers() {
        this.dataHandler = DataHandler.getInstance(this);
        this.dataHandler.setup();

        this.userManager = new UserManager(this);
        this.userManager.setup();

        return true;
    }

    @Override
    public void loadConfig() {
        this.getConfig().initializeOptions(Config.class);
    }

    @Override
    public void loadLang() {
        this.getLangManager().loadMissing(Lang.class);
    }

    @Override
    public void registerCommands(@NotNull GeneralCommand<CoinsEngine> generalCommand) {
        generalCommand.addChildren(new ReloadSubCommand<>(this, Perms.COMMAND_RELOAD));
        generalCommand.addChildren(new ResetCommand(this));
        generalCommand.addChildren(new WipeCommand(this));
    }

    @Override
    public void registerHooks() {

    }

    @Override
    public void registerPermissions() {
        this.registerPermissions(Perms.class);
    }

    @NotNull
    public CurrencyManager getCurrencyManager() {
        return currencyManager;
    }

    @Override
    @NotNull
    public DataHandler getData() {
        return this.dataHandler;
    }

    @NotNull
    @Override
    public UserManager getUserManager() {
        return userManager;
    }
}
