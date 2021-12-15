package com.ustudents.fgen.handlers;

import com.ustudents.fgen.fractals.Fractal;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.Function;

public class PoolCalculationHandler extends CalculationHandler {
    public static final int DEFAULT_PARALLELISM_THRESHOLD = 16192;

    public static ForkJoinPool pool = null;

    public class CalculationTask extends RecursiveAction {
        int startIndex;
        int endIndex;
        int width;
        int height;
        double originX;
        double originY;
        int[][] results;

        public CalculationTask(int startIndex, int endIndex, int width, int height, double originX, double originY, int[][] results) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.width = width;
            this.height = height;
            this.originX = originX;
            this.originY = originY;
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

            invokeAll(new CalculationTask(startIndex, middleIndex, width, height, originX, originY, results),
                      new CalculationTask(middleIndex, endIndex, width, height, originX, originY, results));
        }

        private void computeDirectly() {
            for (int i = startIndex; i < endIndex; i++) {
                int y = i / height;
                int x = i % width;
                Complex z0 = fractal.getZ0(plane, x, y, originX, originY);
                Function<Complex, Complex> f = fractal.getF();
                results[y][x] = calculateDivergenceIndex(z0, f);
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
    public int[][] calculateDivergenceIndexes(int width, int height) {
        verifyPoolStatus();

        int[][] divergenceIndexes = new int[height][width];
        double originX = plane.getOriginX(width);
        double originY = plane.getOriginY(height);
        CalculationTask work = new CalculationTask(0, width * height, width, height, originX, originY, divergenceIndexes);

        pool.invoke(work);

        return divergenceIndexes;
    }

    private void verifyPoolStatus() {
        if (pool != null && pool.getParallelism() != parallelismLevel) {
            pool.shutdown();
            pool = null;
        }

        if (pool == null) {
            pool = new ForkJoinPool(parallelismLevel);
        }
    }
}
