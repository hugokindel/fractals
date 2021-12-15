package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.CalculationHandler;

public abstract class SingleGenerator extends Generator {
    public CalculationHandler handler;

    public SingleGenerator(CalculationHandler handler) {
        this.handler = handler;
    }
}
