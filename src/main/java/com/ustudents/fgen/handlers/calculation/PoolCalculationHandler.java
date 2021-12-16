package com.ustudents.fgen.handlers.calculation;

import com.ustudents.fgen.common.utils.Pool;
import com.ustudents.fgen.fractals.Fractal;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.Function;

public class PoolCalculationHandler extends CalculationHandler {
    public static final int DEFAULT_PARALLELISM_THRESHOLD = 16192;

    private class CalculationTask extends RecursiveAction {
        int startIndex;
        int endIndex;
        int width;
        int height;
        double originX;
        double originY;
        double offsetX;
        double offsetY;

        int[][] results;

        public CalculationTask(int startIndex, int endIndex, int width, int height, double originX, double originY, double offsetX, double offsetY, int[][] results) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.width = width;
            this.height = height;
            this.originX = originX;
            this.originY = originY;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.results = results;
        }

        @Override
        protected void compute() {
            int numCalculations = endIndex - startIndex;

            if (numCalculations < parallelismThreshold) {
                computeDirectly();
                return;
            }

            int middleIndex = (startIndex + endIndex) / 2;

            invokeAll(new CalculationTask(startIndex, middleIndex, width, height, originX, originY, offsetX, offsetY, results),
                      new CalculationTask(middleIndex, endIndex, width, height, originX, originY, offsetX, offsetY, results));
        }

        private void computeDirectly() {
            for (int i = startIndex; i < endIndex; i++) {
                int y = i / height;
                int x = i % width;
                Complex z0 = fractal.getZ0(plane, x, y, originX, originY, offsetX, offsetY);
                Function<Complex, Complex> f = fractal.getF();
                results[y][x] = computeDivergenceIndex(z0, f);
            }
        }
    }

    public int parallelismLevel;
    public int parallelismThreshold;

    public PoolCalculationHandler(Fractal fractal, ComplexPlane plane, int maxIterations, int radius) {
        super(fractal, plane, maxIterations, radius);
        this.parallelismLevel = Runtime.getRuntime().availableProcessors();
        this.parallelismThreshold = DEFAULT_PARALLELISM_THRESHOLD;
    }

    public PoolCalculationHandler(Fractal fractal, ComplexPlane plane, int maxIterations, int radius, int parallelismLevel) {
        this(fractal, plane, maxIterations, radius);
        this.parallelismLevel = parallelismLevel;
    }

    public PoolCalculationHandler(Fractal fractal, ComplexPlane plane, int maxIterations, int radius, int parallelismLevel, int parallelismThreshold) {
        this(fractal, plane, maxIterations, radius, parallelismLevel);
        this.parallelismThreshold = parallelismThreshold;
    }

    @Override
    public int[][] computeDivergenceIndexes(int width, int height, double offsetX, double offsetY) {
        int[][] divergenceIndexes = new int[height][width];
        double originX = plane.getOriginX(width);
        double originY = plane.getOriginY(height);
        CalculationTask work = new CalculationTask(0, width * height, width, height, originX, originY, offsetX, offsetY, divergenceIndexes);

        Pool.get(parallelismLevel).invoke(work);

        return divergenceIndexes;
    }
}
