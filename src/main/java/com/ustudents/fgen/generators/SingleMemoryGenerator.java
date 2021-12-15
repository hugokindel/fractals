package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.CalculationHandler;

public class SingleMemoryGenerator extends SingleGenerator {
    public int[][] divergenceIndexes;

    public SingleMemoryGenerator(CalculationHandler handler) {
        super(handler);
    }

    public void generate(int width, int height) {
        divergenceIndexes = handler.calculateDivergenceIndexes(width, height);
    }
}
