package t.me.p1azmer.engine.coins.currency.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.coins.CoinsEngine;
import t.me.p1azmer.engine.coins.Placeholders;
import t.me.p1azmer.engine.coins.api.currency.Currency;
import t.me.p1azmer.engine.api.config.JOption;
import t.me.p1azmer.engine.api.config.JYML;
import t.me.p1azmer.engine.api.manager.AbstractConfigHolder;
import t.me.p1azmer.engine.api.placeholder.PlaceholderMap;
import t.me.p1azmer.engine.utils.Colorizer;
import t.me.p1azmer.engine.utils.StringUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConfigCurrency extends AbstractConfigHolder<CoinsEngine> implements Currency {

    private String name;
    private String symbol;
    private String format;
    private String[] commandAliases;
    private ItemStack icon;
    private boolean decimal;
    private boolean permissionRequired;
    private boolean transferAllowed;
    private double minTransferAmount;
    private double startValue;
    private double maxValue;
    private boolean vaultEconomy;
    private boolean exchangeAllowed;
    private Map<String, Double> exchangeRates;

    private boolean freeAllowed;
    private double freeValue;

    private final PlaceholderMap placeholderMap;

    public ConfigCurrency(@NotNull CoinsEngine plugin, @NotNull JYML cfg) {
        super(plugin, cfg);
        this.exchangeRates = new HashMap<>();
        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.CURRENCY_ID, this::getId)
            .add(Placeholders.CURRENCY_NAME, this::getName)
            .add(Placeholders.CURRENCY_SYMBOL, this::getSymbol);
    }

    @Override
    public boolean load() {
        this.setName(JOption.create("Name", StringUtil.capitalizeUnderscored(this.getId()),
            "Localized currency name."
        ).mapReader(Colorizer::apply).read(cfg));

        this.setSymbol(JOption.create("Symbol", this.getName(),
            "Currency symbol, like '$'."
        ).mapReader(Colorizer::apply).read(cfg));

        this.setFormat(JOption.create("Format",
            Placeholders.GENERIC_AMOUNT + Placeholders.CURRENCY_SYMBOL,
            "Currency display format.",
            "Use '" + Placeholders.GENERIC_AMOUNT + "' placeholder for amount value.",
            "You can use 'Currency' placeholders: " + Placeholders.WIKI_PLACEHOLDERS
        ).read(cfg));

        this.setCommandAliases(JOption.create("Command_Aliases",
            this.getName(),
            "Custom currency commands. Split with commas."
        ).read(cfg).toLowerCase().split(","));

        this.setIcon(JOption.create("Icon", new ItemStack(Material.GOLD_NUGGET),
            "Currency icon."
        ).read(cfg));

        this.setDecimal(JOption.create("Decimal", false,
            "Sets whether or not currency value can have decimals."
        ).read(cfg));

        this.setPermissionRequired(JOption.create("Permission_Required", false,
            "Sets whether or not players must have '" + this.getPermission() + "' permission to use this currency."
        ).read(cfg));

        this.setTransferAllowed(JOption.create("Transfer_Allowed", true,
            "Sets whether or not players can send this currency to other players."
        ).read(cfg));

        this.setMinTransferAmount(JOption.create("Transfer_Min_Amount", 1D,
            "Sets minimal amount for sending this currency to other players.",
            "Set this to '-1' for no limit."
        ).read(cfg));

        this.setStartValue(JOption.create("Start_Value", 0D,
            "How much of this currency new players will have on their balance?"
        ).read(cfg));

        this.setMaxValue(JOption.create("Max_Value", -1D,
            "Max. possible value that players can have on their balance.",
            "Set this to '-1' to disable."
        ).read(cfg));

        this.setVaultEconomy(JOption.create("Economy.Vault", false,
            "When enabled, uses the Vault API to register the currency as primary server Economy."
        ).read(cfg));

        this.setExchangeAllowed(JOption.create("Exchange.Allowed", true,
            "Sets whether or not this currency can be exchanged for other ones."
        ).read(cfg));

        this.exchangeRates = JOption.forMap("Exchange.Rates",
            (cfg, path, id) -> cfg.getDouble(path + "." + id),
            () -> Map.of("other", 5D),
            "Sets exchange rates for this currency for other ones.",
            "1 of this currency = X of other currency.",
            "Exchange.Rates:",
            "  other: 5",
            "  another: 10"
        ).setWriter((cfg, path, map) -> map.forEach((id, amount) -> cfg.set(path + "." + id, amount))).read(cfg);

        this.setFreeAllowed(JOption.create("Free.Allowed", false,
                        "Sets whether the free currency mode is enabled. Warning.","GameUI plugin is required for work!")
                .read(cfg));
        this.setFreeValue(JOption.create("Free.Value", 1000,
                        "Sets the amount of currency that will be given to the player when a command is entered")
                .read(cfg));
        this.cfg.saveChanges();
        return true;
    }

    @Override
    public void onSave() {
        cfg.set("Name", this.getName());
        cfg.set("Symbol", this.getSymbol());
        cfg.set("Format", this.getFormat());
        cfg.set("Command_Aliases", String.join(",", Arrays.asList(this.getCommandAliases())));
        cfg.setItem("Icon", this.getIcon());
        cfg.set("Decimal", this.isDecimal());
        cfg.set("Permission_Required", this.isPermissionRequired());
        cfg.set("Transfer_Allowed", this.isTransferAllowed());
        cfg.set("Transfer_Min_Amount", this.getMinTransferAmount());
        cfg.set("Start_Value", this.getStartValue());
        cfg.set("Max_Value", this.getMaxValue());
        cfg.set("Economy.Vault", this.isVaultEconomy());
        cfg.set("Exchange.Allowed", this.isExchangeAllowed());
        cfg.remove("Exchange.Rates");
        this.getExchangeRates().forEach((id, rate) -> {
            cfg.set("Exchange.Rates." + id, rate);
        });
        cfg.set("Free.Allowed", this.isFreeAllowed());
        cfg.set("Free.Value", this.getFreeValue());
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = Colorizer.apply(name);
    }

    @NotNull
    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public void setSymbol(@NotNull String symbol) {
        this.symbol = Colorizer.apply(symbol);
    }

    @NotNull
    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public void setFormat(@NotNull String format) {
        this.format = Colorizer.apply(format);
    }

    @NotNull
    @Override
    public String[] getCommandAliases() {
        return commandAliases;
    }

    @Override
    public void setCommandAliases(String[] commandAliases) {
        this.commandAliases = commandAliases;
    }

    @NotNull
    public ItemStack getIcon() {
        return new ItemStack(icon);
    }

    @Override
    public void setIcon(@NotNull ItemStack icon) {
        this.icon = new ItemStack(icon);
    }

    @Override
    public boolean isDecimal() {
        return decimal;
    }

    @Override
    public void setDecimal(boolean decimal) {
        this.decimal = decimal;
    }

    @Override
    public boolean isPermissionRequired() {
        return permissionRequired;
    }

    @Override
    public void setPermissionRequired(boolean permissionRequired) {
        this.permissionRequired = permissionRequired;
    }

    @Override
    public boolean isTransferAllowed() {
        return transferAllowed;
    }

    @Override
    public void setTransferAllowed(boolean transferAllowed) {
        this.transferAllowed = transferAllowed;
    }

    public double getMinTransferAmount() {
        return minTransferAmount;
    }

    @Override
    public void setMinTransferAmount(double minTransferAmount) {
        this.minTransferAmount = minTransferAmount;
    }

    @Override
    public double getStartValue() {
        return startValue;
    }

    @Override
    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    @Override
    public double getMaxValue() {
        return maxValue;
    }

    @Override
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public boolean isVaultEconomy() {
        return vaultEconomy;
    }

    @Override
    public void setVaultEconomy(boolean vaultEconomy) {
        this.vaultEconomy = vaultEconomy;
    }

    public boolean isExchangeAllowed() {
        return exchangeAllowed;
    }

    @Override
    public void setExchangeAllowed(boolean exchangeAllowed) {
        this.exchangeAllowed = exchangeAllowed;
    }

    @NotNull
    public Map<String, Double> getExchangeRates() {
        return exchangeRates;
    }

    @Override
    public boolean isFreeAllowed() {
        return this.freeAllowed;
    }

    @Override
    public void setFreeAllowed(boolean freeAllowed) {
        this.freeAllowed = freeAllowed;
    }

    @Override
    public double getFreeValue() {
        return freeValue;
    }

    @Override
    public void setFreeValue(double freeValue) {
        this.freeValue = freeValue;
    }
}
