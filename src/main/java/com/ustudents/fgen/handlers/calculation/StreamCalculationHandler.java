package com.ustudents.fgen.handlers.calculation;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.fractals.Fractal;
import com.ustudents.fgen.maths.ComplexPlane;

import static java.util.stream.IntStream.range;

@JsonSerializable(serializeClassName = true)
public class StreamCalculationHandler extends CalculationHandler {
    public StreamCalculationHandler() {

    }

    public StreamCalculationHandler(Fractal fractal, ComplexPlane complexPlane, int maxIterations, int radius) {
        super(fractal, complexPlane, maxIterations, radius);
    }

    @Override
    public int[][] computeDivergenceIndexes(int width, int height, double offsetX, double offsetY) {
        double originX = plane.getOriginX(width);
        double originY = plane.getOriginY(height);

        return range(0, height)
                .parallel()
                .mapToObj(y -> range(0, width)
                        .parallel()
                        .map(x -> computeDivergenceIndex(fractal.getZ0(plane, x, y, originX, originY, offsetX, offsetY), fractal.getF()))
                        .toArray())
                .toArray(int[][]::new);
    }
}
