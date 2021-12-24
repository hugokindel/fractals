package com.ustudents.fgen.common.utils;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class TextFieldUtil {
    public static UnaryOperator<TextFormatter.Change> intFilter = change -> {
        String text = change.getControlNewText();

        if (text.matches("^[0-9]*$")) {
            return change;
        }

        return null;
    };

    public static UnaryOperator<TextFormatter.Change> intWithNegFilter = change -> {
        String text = change.getControlNewText();

        if (text.matches("^-?[0-9]*$")) {
            return change;
        }

        return null;
    };

    public static UnaryOperator<TextFormatter.Change> doubleWithNegFilter = change -> {
        String text = change.getControlNewText();

        if (text.matches("^-?[0-9]*\\.?[0-9]*$")) {
            return change;
        }

        return null;
    };

    public static UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
        String text = change.getControlNewText();

        if (text.matches("^[0-9]*\\.?[0-9]*$")) {
            return change;
        }

        return null;
    };
}
