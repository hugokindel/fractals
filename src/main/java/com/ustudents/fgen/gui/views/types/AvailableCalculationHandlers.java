package com.ustudents.fgen.gui.views.types;

import com.ustudents.fgen.common.utils.Pool;
import com.ustudents.fgen.handlers.calculation.*;
import com.ustudents.fgen.handlers.color.*;

public enum AvailableCalculationHandlers {
    SIMPLE("Simple"),
    POOL("Pool"),
    STREAM("Stream"),
    BUDDHA_SIMPLE("Buddha Simple"),
    BUDDHA_STREAM("Buddha Stream");

    private final String name;

    AvailableCalculationHandlers(String type) {
        this.name = type;
    }

    public static AvailableCalculationHandlers fromCalculationHandlers(CalculationHandler calculationHandler) {
        if (calculationHandler instanceof PoolCalculationHandler) {
            return AvailableCalculationHandlers.POOL;
        } else if (calculationHandler instanceof StreamCalculationHandler) {
            return AvailableCalculationHandlers.STREAM;
        } else if (calculationHandler instanceof BuddhaSimpleCalculationHandler) {
            return AvailableCalculationHandlers.BUDDHA_SIMPLE;
        } else if (calculationHandler instanceof  BuddhaStreamCalculationHandler) {
            return AvailableCalculationHandlers.BUDDHA_STREAM;
        }

        return AvailableCalculationHandlers.SIMPLE;
    }

    public CalculationHandler toCalculationHandler(CalculationHandler oldCalculationHandler) {
        CalculationHandler calculationHandler;

        switch (this) {
            case POOL -> {
                calculationHandler = new PoolCalculationHandler();
            }
            case STREAM -> {
                calculationHandler = new StreamCalculationHandler();
            }
            case BUDDHA_SIMPLE -> {
                calculationHandler = new BuddhaSimpleCalculationHandler();
            }
            case BUDDHA_STREAM -> {
                calculationHandler = new BuddhaStreamCalculationHandler();
            }
            default -> {
                calculationHandler = new SimpleCalculationHandler();
            }
        }

        calculationHandler.plane = oldCalculationHandler.plane;
        calculationHandler.fractal = oldCalculationHandler.fractal;
        calculationHandler.maxIterations = oldCalculationHandler.maxIterations;
        calculationHandler.radius = oldCalculationHandler.radius;

        return calculationHandler;
    }

    @Override
    public String toString() {
        return name;
    }
}