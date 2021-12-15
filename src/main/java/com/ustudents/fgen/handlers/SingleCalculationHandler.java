package com.ustudents.fgen.handlers;

import com.ustudents.fgen.fractals.Fractal;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

import java.util.function.Function;

public class SingleCalculationHandler extends CalculationHandler {
    public SingleCalculationHandler(Fractal fractal, ComplexPlane complexPlane, int maxIterations, int radius) {
        super(fractal, complexPlane, maxIterations, radius);
    }

    @Override
    public int[][] calculateDivergenceIndexes(int width, int height) {
        int[][] divergenceIndexes = new int[height][width];
        double originX = plane.getOriginX(width);
        double originY = plane.getOriginY(height);

        for (int y = 0 ; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Complex z0 = fractal.getZ0(plane, x, y, originX, originY);
                Function<Complex, Complex> f = fractal.getF();
                divergenceIndexes[y][x] = calculateDivergenceIndex(z0, f);
            }
        }

        return divergenceIndexes;
    }
}
