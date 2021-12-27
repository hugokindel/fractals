package com.ustudents.fgen.handlers.calculation;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.common.utils.Pool;
import com.ustudents.fgen.fractals.Fractal;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.Function;

/**
 * Calculates the matrix of divergence indexes within a ForKJoinPool.
 * Most optimized method available here.
 */
@JsonSerializable(serializeClassName = true)
public class PoolCalculationHandler extends CalculationHandler {
    public static final int DEFAULT_PARALLELISM_THRESHOLD = 16192;

    public Integer getParallelismLevel() {
        return parallelismLevel;
    }

    public Integer getParallelismThreshold() {
        return parallelismThreshold;
    }

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
                int y = i / width;
                int x = i % width;
                Complex z0 = fractal.getZ0(plane, x, y, originX, originY, offsetX, offsetY);
                Function<Complex, Complex> f = fractal.getF(plane, x, y, originX, originY, offsetX, offsetY);
                results[y][x] = computeDivergenceIndex(z0, f);
            }
        }
    }

    @JsonSerializable(necessary = false)
    private Integer parallelismLevel = Runtime.getRuntime().availableProcessors();

    @JsonSerializable(necessary = false)
    private Integer parallelismThreshold = DEFAULT_PARALLELISM_THRESHOLD;

    public PoolCalculationHandler() {

    }

    public PoolCalculationHandler(Fractal fractal, ComplexPlane plane, int maxIterations, int radius) {
        super(fractal, plane, maxIterations, radius);
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

    public void setParallelismLevel(Integer parallelismLevel) {
        if (parallelismLevel == 0 || parallelismLevel > Runtime.getRuntime().availableProcessors()) {
            this.parallelismLevel = Runtime.getRuntime().availableProcessors();
        } else {
            this.parallelismLevel = parallelismLevel;
        }
    }

    public void setParallelismThreshold(Integer parallelismThreshold) {
        if (parallelismThreshold < DEFAULT_PARALLELISM_THRESHOLD) {
            this.parallelismThreshold = DEFAULT_PARALLELISM_THRESHOLD;
        } else {
            this.parallelismThreshold = parallelismThreshold;
        }
    }
}
