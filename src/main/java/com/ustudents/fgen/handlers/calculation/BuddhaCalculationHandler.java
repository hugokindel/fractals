package com.ustudents.fgen.handlers.calculation;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.fractals.Fractal;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

@JsonSerializable(serializeClassName = true)
public abstract class BuddhaCalculationHandler extends CalculationHandler{
    public BuddhaCalculationHandler() {

    }

    public BuddhaCalculationHandler(Fractal fractal, ComplexPlane complexPlane, int maxIterations, int radius) {
        super(fractal, complexPlane, maxIterations, radius);
    }

    // genere une liste de coordonnées (x,y) aléatoires dans les limites de l'image
    protected ArrayList<Pair<Integer,Integer>> getRandomConstantList(int maxIterationsConstant, int width, int height, double offsetX, double offsetY){
        double originX = plane.getOriginX(width);
        double originY = plane.getOriginY(height);
        ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
        Random random = new Random();

        for (int h = 0 ; h < maxIterationsConstant; h++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Pair<Integer,Integer> p = new Pair<>(x,y);
            list.add(p);
        }

        return list;
    }

    // genere l'indice de divergence et la liste des Zn obtenu dans la boucle while
    protected Pair<Integer,ArrayList<Complex>> computeDivergenceIndexList(Complex z0, Function<Complex, Complex> f) {
        int n = 0;
        Complex zn = z0.clone();
        ArrayList<Complex> list = new ArrayList<>();

        while (zn.getModulus() <= radius && n < maxIterations) {
            list.add(zn);
            zn = f.apply(zn);
            n++;
        }

        return new Pair<>(n,list);
    }

}
