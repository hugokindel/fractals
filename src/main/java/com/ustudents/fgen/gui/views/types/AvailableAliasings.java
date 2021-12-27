package com.ustudents.fgen.gui.views.types;

import com.ustudents.fgen.format.AliasingType;
import com.ustudents.fgen.handlers.color.*;

public enum AvailableAliasings {
    X1("x1"),
    X2("x2"),
    X4("x4"),
    X8("x8"),
    X16("x16");

    private final String name;

    AvailableAliasings(String type) {
        this.name = type;
    }

    public static AvailableAliasings fromAliasingType(AliasingType aliasingType) {
        switch (aliasingType) {
            case X2 -> {
                return X2;
            }
            case X4 -> {
                return X4;
            }
            case X8 -> {
                return X8;
            }
            case X16 -> {
                return X16;
            }
        }

        return X1;
    }

    public AliasingType toAliasingType() {
        switch (this) {
            case X2 -> {
                return AliasingType.X2;
            }
            case X4 -> {
                return AliasingType.X4;
            }
            case X8 -> {
                return AliasingType.X8;
            }
            case X16 -> {
                return AliasingType.X16;
            }
        }

        return AliasingType.X1;
    }

    @Override
    public String toString() {
        return name;
    }
}