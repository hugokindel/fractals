package com.ustudents.fractals.common.cli.option;

import com.ustudents.fractals.common.cli.print.Out;

/** Define a parsable type from an option in command line. */
public interface Parsable {
    /**
     * Parse the option value to the given type.
     *
     * @param value The value to parse.
     * @return the parsed object.
     */
    public static Object parseFromOption(String value) {
        Out.printlnError("Call from an unset parsable type.");

        return null;
    }
}
