package com.ustudents.fgen.common;

import com.ustudents.fgen.common.options.Runnable;
import com.ustudents.fgen.common.options.Command;
import com.ustudents.fgen.common.logs.Out;

public abstract class Program extends Runnable {
    /** The name of the program instance. */
    protected static String instanceName = "program";

    /** The program instance. */
    protected static Program instance;

    /** Runs this program. */
    @Override
    public int run(String[] args) {
        Program.instance = this;

        if (getClass().getAnnotation(Command.class) != null) {
            instanceName = getClass().getAnnotation(Command.class).name();
        }

        Out.start(args, false, true);

        if (!parse(args, getClass())) {
            return 1;
        }

        int exitCode = 0;

        if (!showHelp && !showVersion) {
            exitCode = main(args);
        }

        Out.end();

        return exitCode;
    }

    /** @return the instance's name. */
    public static String getInstanceName() {
        return instanceName;
    }

    /** @return the program. */
    public static Program get() {
        return instance;
    }

    /** Main function of the program. */
    protected abstract int main(String[] args);
}
