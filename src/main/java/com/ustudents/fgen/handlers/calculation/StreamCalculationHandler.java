package com.ustudents.fgen.handlers.calculation;

import com.ustudents.fgen.fractals.Fractal;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.IntStream.range;

public class StreamCalculationHandler extends CalculationHandler{
    public StreamCalculationHandler(Fractal fractal, ComplexPlane complexPlane, int maxIterations, int radius) {
        super(fractal, complexPlane, maxIterations, radius);
    }

    @Override
    public int[][] computeDivergenceIndexes(int width, int height, double offsetX, double offsetY) {
        Complex[][] complexesIndexes = new Complex[height][width];
        double originX = plane.getOriginX(width);
        double originY = plane.getOriginY(height);

        for (int y = 0 ; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Complex z0 = fractal.getZ0(plane, x, y, originX, originY, offsetX, offsetY);
                complexesIndexes[y][x] = z0;
            }
        }

        return (int[][]) range(0, complexesIndexes.length)
                .parallel()
                .mapToObj(x -> Arrays.stream(complexesIndexes[x])
                        .parallel()
                        .mapToInt(z0 -> computeDivergenceIndex(z0, fractal.getF()))
                        .toArray())
                .toArray();
    }
}
