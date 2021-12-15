package com.ustudents.fgen.fractals;

import java.util.function.Function;

public class SingleCalculationHandler extends CalculationHandler {
    public SingleCalculationHandler(Fractal fractal, ComplexPlane complexPlane, int maxIterations, int radius) {
        super(fractal, complexPlane, maxIterations, radius);
    }

    @Override
    public int[][] calculateDivergenceIndexes(int width, int height) {
        int[][] divergenceIndexes = new int[height][width];
        double originX = getPlane().getOriginX(width);
        double originY = getPlane().getOriginY(height);

        for (int y = 0 ; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Complex z0 = getFractal().getZ0(getPlane(), x, y, originX, originY);
                Function<Complex, Complex> f = getFractal().getF();
                divergenceIndexes[y][x] = calculateDivergenceIndex(z0, f);
            }
        }

        return divergenceIndexes;
    }
}
