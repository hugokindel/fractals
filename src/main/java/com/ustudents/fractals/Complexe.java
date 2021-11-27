package com.ustudents.fractals;

public class Complexe {
    public static Complexe ZERO = new Complexe(0,0);
    public static Complexe ONE = new Complexe(1,0);
    public static Complexe I = new Complexe(0,1);

    private final double r;
    private final double i;

    public Complexe(double re,double im){
        r = re;
        i = im;
    }

    public String toString(){
        return " Z = " + r + " + i" + i;
    }

    public Complexe somme(Complexe c){
        return new Complexe(r+c.r,i+c.i);
    }

    public Complexe soustraction(Complexe c){
        return new Complexe(r-c.r,i-c.i);
    }

    public Complexe multiplication(Complexe c){
        return new Complexe((r*c.r)-(i*c.i),(r*c.i) + (i*c.r));
    }

    public Complexe division(Complexe c){
        double diviseur = c.r*c.r + c.i*c.i;
        return new Complexe((r*c.r)+(i*c.i)/diviseur ,(i*c.r - c.i*r)/diviseur);
    }

    public boolean equals(Object other){
        if (other instanceof Complexe){
            return (r == ((Complexe) other).r && i == ((Complexe)other).i);
        }
        return false;
    }

    public double getReel(){
        return r;
    }

    public double getImaginaire(){
        return i;
    }

    public Complexe getConjugaison(){
        return new Complexe(r,-i);
    }

    public double getModule(){
        return Math.sqrt(r*r + i*i);
    }

    public static Complexe fromPolarCoordinates(double rho, double theta){
        return new Complexe(rho*Math.cos(theta),rho*Math.sin(theta));
    }

    public Complexe copie(){
        return new Complexe(this.r,this.i);
    }


}