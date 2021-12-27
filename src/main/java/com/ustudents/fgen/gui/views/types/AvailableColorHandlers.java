package com.ustudents.fgen.gui.views.types;

import com.ustudents.fgen.handlers.color.*;

public enum AvailableColorHandlers {
    BASIC("Basic"),
    VERY_RED("Very Red"),
    HSB("Hsb"),
    DARK_PSYCHEDELIC("Dark Psychedelic"),
    LIGHT_PSYCHEDELIC("Light Psychedelic"),
    BUDDHA("Buddha");

    private final String name;

    AvailableColorHandlers(String type) {
        this.name = type;
    }

    public static AvailableColorHandlers fromColorHandler(ColorHandler colorHandler) {
        if (colorHandler instanceof VeryRedColorHandler) {
            return AvailableColorHandlers.VERY_RED;
        } else if (colorHandler instanceof HsbColorHandler) {
            return AvailableColorHandlers.HSB;
        } else if (colorHandler instanceof DarkPsychedelicColorHandler) {
            return AvailableColorHandlers.DARK_PSYCHEDELIC;
        } else if (colorHandler instanceof  LightPsychedelicColorHandler) {
            return AvailableColorHandlers.LIGHT_PSYCHEDELIC;
        } else if (colorHandler instanceof  BuddhaColorHandler) {
            return AvailableColorHandlers.BUDDHA;
        }

        return AvailableColorHandlers.BASIC;
    }

    public ColorHandler toColorHandler() {
        switch (this) {
            case VERY_RED -> {
                return new VeryRedColorHandler();
            }
            case HSB -> {
                return new HsbColorHandler();
            }
            case DARK_PSYCHEDELIC -> {
                return new DarkPsychedelicColorHandler();
            }
            case LIGHT_PSYCHEDELIC -> {
                return new LightPsychedelicColorHandler();
            }
            case BUDDHA -> {
                return new BuddhaColorHandler();
            }
            default -> {
                return new BasicColorHandler();
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}