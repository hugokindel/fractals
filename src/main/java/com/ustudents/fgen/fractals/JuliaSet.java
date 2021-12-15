package com.ustudents.fgen.fractals;

import java.util.function.Function;

public class JuliaSet extends Fractal {
    public Complex c;

    public JuliaSet(Complex c) {
        this.c = c;
    }

    @Override
    public Complex getZ0(ComplexPlane plane, double x, double y, double originX, double originY) {
        return new Complex(originX + plane.getZoom() * x, originY - plane.getZoom() * y);
    }

    @Override
    public Function<Complex, Complex> getF() {
        return (Complex z) -> z.multiply(z).add(c);
    }
}
