package t.me.p1azmer.engine.coins.command;

import t.me.p1azmer.engine.api.command.CommandFlag;

public class CommandFlags {

    public static final CommandFlag<Boolean> SILENT = CommandFlag.booleanFlag("s");
    public static final CommandFlag<Boolean> NO_SAVE = CommandFlag.booleanFlag("nosave");
}
