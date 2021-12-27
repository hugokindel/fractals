package com.ustudents.fgen.handlers.calculation;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.fractals.Fractal;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.function.Function;

/** Compute the divergence indexes by using the Buddha method using a single stream. */
@JsonSerializable(serializeClassName = true)
public class BuddhaSimpleCalculationHandler extends BuddhaCalculationHandler {
    public BuddhaSimpleCalculationHandler() {

    }

    public BuddhaSimpleCalculationHandler(Fractal fractal, ComplexPlane complexPlane, int maxIterations, int radius) {
        super(fractal, complexPlane, maxIterations, radius);
    }

    @Override
    public int[][] computeDivergenceIndexes(int width, int height, double offsetX, double offsetY) {
        int[][] divergenceIndexes = new int[height][width];
        double originX = plane.getOriginX(width);
        double originY = plane.getOriginY(height);
        double step = plane.getStep();
        /* Calculates 1000000 random coordinates (x, y) in the image's size limits. */
        ArrayList<Pair<Integer, Integer>> list = getRandomConstantList(1000000,width,height,offsetX,offsetY);

        /* For each coordinate, we calculate the associated complex in the plane. */
        for(Pair<Integer,Integer> p : list) {
            int x = p.getKey();
            int y = p.getValue();
            Complex z0 = fractal.getZ0(plane, x, y, originX, originY, offsetX, offsetY);
            Function<Complex, Complex> f = fractal.getF(plane, x, y, originX, originY, offsetX, offsetY);
            Pair<Integer,ArrayList<Complex>> pAdd = computeDivergenceIndexList(z0, f);

            /* if it converges, we inspect every zn obtained. */
            if (pAdd.getKey() >= maxIterations){
                for( Complex zAdd : pAdd.getValue()){
                    /* Calculate the coordinates of the complex. */
                    int xAdd = (int) (((zAdd.real - originX) / step) + offsetX);
                    int yAdd = (int) ((((zAdd.imaginary - originY) * (-1)) / step) + offsetY);

                    /* If those coordinates are part of our image. */
                    if (xAdd >= 0 && xAdd < width && yAdd >= 0 && yAdd < height){
                        /* Increments the counter by 1 (they're all at 0 by default). */
                        divergenceIndexes[yAdd][xAdd]++;
                    }
                }
            }
        }

        return divergenceIndexes;
    }
}
