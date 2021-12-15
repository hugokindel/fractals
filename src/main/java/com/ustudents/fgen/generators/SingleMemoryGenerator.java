package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.CalculationHandler;

public class SingleMemoryGenerator extends Generator {
    public CalculationHandler handler;
    public int[][] divergenceIndexes;

    public SingleMemoryGenerator(CalculationHandler handler) {
        this.handler = handler;
    }

    public void generate(int width, int height) {
        divergenceIndexes = handler.calculateDivergenceIndexes(width, height);
    }
}
