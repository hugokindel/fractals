package com.ustudents.fgen.fractals;

import java.util.function.Function;

public abstract class CalculationHandler {
    public ComplexPlane plane;
    public Fractal fractal;
    public int maxIterations;
    public int radius;

    public CalculationHandler(Fractal fractal, ComplexPlane plane, int maxIterations, int radius) {
        this.fractal = fractal;
        this.plane = plane;
        this.maxIterations = maxIterations;
        this.radius = radius;
    }

    public abstract int[][] calculateDivergenceIndexes(int width, int height);

    protected int calculateDivergenceIndex(Complex z0, Function<Complex, Complex> f) {
        int n = 0;
        Complex zn = z0.clone();

        while (zn.getModulus() <= radius && n < maxIterations) {
            zn = f.apply(zn);
            n++;
        }

        return n;
    }
}
