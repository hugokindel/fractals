package com.ustudents.fgen.handlers.calculation;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.fractals.Fractal;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.function.Function;

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
        ArrayList<Pair<Integer, Integer>> list = getRandomConstantList(1000000,width,height,offsetX,offsetY);

        for( Pair<Integer,Integer> p : list){
            int x = p.getKey();
            int y = p.getValue();
            Complex z0 = fractal.getZ0(plane, x, y, originX, originY, offsetX, offsetY);
            Function<Complex, Complex> f = fractal.getF(plane, x, y, originX, originY, offsetX, offsetY);
            Pair<Integer,ArrayList<Complex>> pAdd = computeDivergenceIndexList(z0, f);
            if (pAdd.getKey() >= maxIterations){
                for( Complex zAdd : pAdd.getValue()){
                    int xAdd = (int) (((zAdd.real - originX) / step) + offsetX);
                    int yAdd = (int) ((((zAdd.imaginary - originY) * (-1)) / step) + offsetY);
                    if (xAdd >= 0 && xAdd < width && yAdd >= 0 && yAdd < height){
                        divergenceIndexes[yAdd][xAdd]++;
                    }
                }
            }
        }

        return divergenceIndexes;
    }
}
