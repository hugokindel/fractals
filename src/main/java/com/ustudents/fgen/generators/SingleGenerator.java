package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.calculation.CalculationHandler;

public abstract class SingleGenerator extends Generator {
    public CalculationHandler calculationHandler;

    public SingleGenerator(CalculationHandler calculationHandler) {
        this.calculationHandler = calculationHandler;
    }
}
