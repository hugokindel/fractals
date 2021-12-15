package com.ustudents.fgen.fractals;

import javafx.util.Pair;

import java.util.function.Function;

public abstract class CalculationHandler {
    private ComplexPlane plane;
    private Fractal fractal;
    private int maxIterations;
    private int radius;

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

    public ComplexPlane getPlane() {
        return plane;
    }

    public void setPlane(ComplexPlane plane) {
        this.plane = plane;
    }

    public void setFractal(Fractal fractal) {
        this.fractal = fractal;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Fractal getFractal() {
        return fractal;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public int getRadius() {
        return radius;
    }
}
