package com.ustudents.fgen.generators;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.benchmark.Benchmark;
import com.ustudents.fgen.handlers.calculation.CalculationHandler;

public class SingleMemoryGenerator extends SingleGenerator {
    public int[][] divergenceIndexes;

    public SingleMemoryGenerator(CalculationHandler calculationHandler) {
        super(calculationHandler);
    }

    public void generate(int width, int height, double offsetX, double offsetY) {
        Benchmark benchmark = new Benchmark();
        divergenceIndexes = calculationHandler.computeDivergenceIndexes(width, height, offsetX, offsetY);
        FGen.calculationHandlerDuration = FGen.calculationHandlerDuration.plus(benchmark.end());
    }
}
