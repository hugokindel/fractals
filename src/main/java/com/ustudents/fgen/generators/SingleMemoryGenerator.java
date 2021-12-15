package com.ustudents.fgen.generators;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.benchmark.Benchmark;
import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.handlers.calculation.CalculationHandler;

import java.time.Duration;

public class SingleMemoryGenerator extends SingleGenerator {
    public int[][] divergenceIndexes;

    public SingleMemoryGenerator(CalculationHandler calculationHandler) {
        super(calculationHandler);
    }

    public void generate(int width, int height) {
        Benchmark benchmark = new Benchmark();
        divergenceIndexes = calculationHandler.calculateDivergenceIndexes(width, height);
        FGen.calculationHandlerDuration = FGen.calculationHandlerDuration.plus(benchmark.end());
    }
}
