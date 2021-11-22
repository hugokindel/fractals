package com.ustudents.fractals.common;

import com.ustudents.fractals.common.cli.option.Runnable;
import com.ustudents.fractals.common.cli.option.annotation.Command;
import com.ustudents.fractals.common.cli.print.Out;
import com.ustudents.fractals.common.utility.Resources;

public abstract class BaseProgram extends Runnable {
    /** The name of the game instance. */
    protected static String instanceName = "program";

    protected static int exitCode = 0;

    /** The game instance. */
    private static BaseProgram instance;

    /** @return the instance's name. */
    public static String getInstanceName() {
        return instanceName;
    }

    @Override
    public int run(String[] args) {
        BaseProgram.instance = this;

        if (getClass().getAnnotation(Command.class) != null) {
            instanceName = getClass().getAnnotation(Command.class).name();
        }

        Out.start(args, false, true);

        if (!readArguments(args, getClass())) {
            return 1;
        }

        initialize();
        main(args);
        destroy();
        Out.end();

        return exitCode;
    }

    protected String getConfigComment() {
        return "// This file contains the project configuration, feel free to edit what you need.\n" +
               "//\n" +
               "// Have fun!\n\n";
    }

    protected abstract void main(String[] args);

    private void initialize() {
        Resources.loadConfig();
    }

    private void destroy() {
        Resources.saveConfig(getConfigComment());
    }

    /** @return the game. */
    public static BaseProgram get() {
        return instance;
    }
}
