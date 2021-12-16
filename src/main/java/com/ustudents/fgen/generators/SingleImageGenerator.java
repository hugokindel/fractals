package com.ustudents.fgen.generators;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.benchmark.Benchmark;
import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.handlers.calculation.CalculationHandler;
import com.ustudents.fgen.handlers.image.ImageHandler;

import java.awt.image.BufferedImage;

public class SingleImageGenerator extends SingleMemoryGenerator {
    public BufferedImage bufferedImage;
    ImageHandler imageHandler;

    public SingleImageGenerator(CalculationHandler calculationHandler, ImageHandler imageHandler) {
        super(calculationHandler);
        this.imageHandler = imageHandler;
    }

    @Override
    public void generate(int width, int height, double offsetX, double offsetY) {
        super.generate(width, height, offsetX, offsetY);

        Benchmark benchmark = new Benchmark();
        bufferedImage = imageHandler.fillImage(divergenceIndexes, calculationHandler.maxIterations);
        FGen.imageHandlerDuration = FGen.imageHandlerDuration.plus(benchmark.end());
    }
}
