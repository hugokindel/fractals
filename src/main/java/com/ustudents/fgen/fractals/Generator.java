package com.ustudents.fgen.fractals;

public abstract class Generator {
    public CalculationHandler handler;

    public Generator(CalculationHandler handler) {
        this.handler = handler;
    }

    public abstract void generate();
}
