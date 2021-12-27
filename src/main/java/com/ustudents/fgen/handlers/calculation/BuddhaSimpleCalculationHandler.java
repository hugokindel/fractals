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
        // obtient 1000000 coordonnées (x,y) aléatoires dans les limites de l'image
        ArrayList<Pair<Integer, Integer>> list = getRandomConstantList(1000000,width,height,offsetX,offsetY);

        for( Pair<Integer,Integer> p : list){
            // pour chaque coordonnées on calcule le Complex dans notre plan complexe
            int x = p.getKey();
            int y = p.getValue();
            Complex z0 = fractal.getZ0(plane, x, y, originX, originY, offsetX, offsetY);
            Function<Complex, Complex> f = fractal.getF(plane, x, y, originX, originY, offsetX, offsetY);
            // obtient l'indice de divergence et la liste des Zn obtenu dans la boucle while pour ce Complex aléatoire
            Pair<Integer,ArrayList<Complex>> pAdd = computeDivergenceIndexList(z0, f);
            // si il converge (indice = maxIterations) alors on regarde tous les Zn obtenus durant le calcul
            if (pAdd.getKey() >= maxIterations){
                for( Complex zAdd : pAdd.getValue()){
                    // on calcul les coordonnées (x,y) du Complex
                    int xAdd = (int) (((zAdd.real - originX) / step) + offsetX);
                    int yAdd = (int) ((((zAdd.imaginary - originY) * (-1)) / step) + offsetY);
                    // si ces coordonnées correspondent a un pixel de notre image (cad appartient a notre matrice de pixel)
                    if (xAdd >= 0 && xAdd < width && yAdd >= 0 && yAdd < height){
                        // on increment le compteur de 1 (ils sont tous a 0 par défaut)
                        divergenceIndexes[yAdd][xAdd]++;
                    }
                }
            }
        }

        return divergenceIndexes;
    }
}
