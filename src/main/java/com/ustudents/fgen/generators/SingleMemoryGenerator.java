package com.ustudents.fgen.generators;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.benchmark.Benchmark;
import com.ustudents.fgen.handlers.calculation.CalculationHandler;

public class SingleMemoryGenerator extends SingleGenerator {
    public int[][] divergenceIndexes = null;

    public SingleMemoryGenerator() {

    }

    public SingleMemoryGenerator(int width, int height, double offsetX, double offsetY, CalculationHandler calculationHandler) {
        super(width, height, offsetX, offsetY, calculationHandler);
    }

    public void generate() {
        Benchmark benchmark = new Benchmark();
        divergenceIndexes = calculationHandler.computeDivergenceIndexes(width, height, offsetX, offsetY);
        FGen.calculationHandlerDuration = FGen.calculationHandlerDuration.plus(benchmark.end());
    }
}
