package t.me.p1azmer.engine.coins.util;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.coins.config.Config;
import t.me.p1azmer.engine.utils.StringUtil;

public class CoinsUtils {

    public static double getAmountFromInput(@NotNull String input) {
        input = input.toLowerCase();
        double multiplier = 1D;

        for (var shortcut : Config.AMOUNT_SHORTCUTS.get().values()) {
            while (input.endsWith(shortcut.getLiteral())) {
                input = input.substring(0, input.length() - shortcut.getLiteral().length());
                multiplier *= shortcut.getMultiplier();
            }
        }

        double value = StringUtil.getDouble(input, 0D) * multiplier;
        if (Double.isInfinite(value) || Double.isNaN(value)) value = 0D;

        return value;
    }
}
