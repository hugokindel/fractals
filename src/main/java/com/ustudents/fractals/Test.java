package com.ustudents.fractals;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;



public class Test {

    static int MAX_ITER = 1000;
    static int RADIUS = 2;

    public static int divergenceIndex(Complexe z0, Function<Complexe, Complexe> f){
        int ite = 0;
        Complexe zn = z0.copie();
        while (ite < MAX_ITER && (int)(zn.getModule()) <= RADIUS){
            zn = f.apply(zn);
            ite++;
        }
        return ite;
    }

    public static void main (String[] args){

        // doivent etre choisi par l'utilisateur
        // constante c et fonction f  ( au moins la constante c )
        // rectangle de travail sur le plan complexe
        // pas de discrétisation
        // taille de la matrice de pixels
        // nombre d'iteration max dans le calcul de l'indice de divergence
        // methode de coloration de pixel en fonction de la divergence
        // eventuellement du fichier pour sauvegarder l'image

        // constante c et fonction f
        //Complexe c = new Complexe(-0.7269,0.1889);
        Complexe c = new Complexe(0.285,0.01);
        Function<Complexe,Complexe> f = (Complexe z) -> c.somme(z.multiplication(z));

        // rectangle sur le plan complexe
        Complexe rectangleStart = new Complexe(-1,1);
        Complexe rectangleEnd = new Complexe(1,-1);

        //calcul du point en bas a gauche TODO
        // -1,-1 position point en bas a gauche

        //hauteur et longueur du rectangle puis passage en int
        double hauteur = Math.abs(rectangleEnd.getReel() - rectangleStart.getReel());
        double longueur = Math.abs(rectangleEnd.getImaginaire() - rectangleStart.getImaginaire());

        // pas de discrétisation
        double pas = 0.01;

        int h = (int)(hauteur/pas);
        int l = (int)(longueur/pas);

        //creation de l'image
        var img = new BufferedImage(l,h,BufferedImage.TYPE_INT_RGB);

        // coloration de tous les pixels TODO multithreading avec ForkJoinPool
        for (int x = 0 ; x < h; x++){
            for (int y = 0 ; y < l; y++){

                // -1,-1 position point en bas a gauche
                double complexeX = -1.0 + pas*x;
                double complexeY = -1.0 + pas*y;
                Complexe z0 = new Complexe(complexeX,complexeY);

                int ind = divergenceIndex(z0,f);
                boolean diverge = ind < MAX_ITER;

                // RGB => 256 | 256 | 256
                // 256 / 3 = 85,333
                int couleur;
                if (diverge) {
                    // tentative de couleur en fct de divergence perso
                    //couleur = ((ind % 257) << 16) | (((ind + 85) % 257) << 8) | ((ind + 170) % 257);

                    //tentatives du pdf

                    /**
                    int r = (255*ind)/MAX_ITER;
                    int g = 0;
                    int b = 0;
                    couleur = (r << 16) | (g << 8) | b;*/

                    couleur = Color.HSBtoRGB((float)ind/MAX_ITER, 0.7f, 0.7f);

                } else {
                    /** turquoise
                    int r = 64;
                    int g = 224;
                    int b = 208; */
                    int r = 0;
                    int g = 0;
                    int b = 0;
                    couleur = (r << 16) | (g << 8) | b;
                }

                img.setRGB(x,y,couleur);
            }
        }

        /**
        int r = 64;
        int g = 224;
        int b = 208; //turquoise
        int col = (r << 16) | (g << 8) | b;
        img.setRGB(30,40,col);
         */


        //sauvegarde de l'image en .png
        File file = new File("Myfile.png");
        try {
            ImageIO.write(img,"PNG",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
