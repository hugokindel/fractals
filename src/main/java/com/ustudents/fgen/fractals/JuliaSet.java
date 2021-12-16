package com.ustudents.fgen.fractals;

import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

import java.util.function.Function;

public class JuliaSet extends Fractal {
    protected Complex c;

    public JuliaSet(Complex c) {
        this.c = c;
    }

    @Override
    public Complex getZ0(ComplexPlane plane, double x, double y, double originX, double originY, double offsetX, double offsetY) {
        return new Complex(originX + plane.getZoom() * (x - offsetX), originY - plane.getZoom() * (y - offsetY));
    }

    @Override
    public Function<Complex, Complex> getF() {
        return (Complex z) -> z.multiply(z).add(c);
    }
}
