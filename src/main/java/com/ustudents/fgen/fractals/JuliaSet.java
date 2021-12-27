package com.ustudents.fgen.fractals;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

import java.util.function.Function;

/** Represents a fractal of the Julia set. */
@JsonSerializable(serializeClassName = true)
public class JuliaSet extends Fractal {
    @JsonSerializable
    public Complex c;

    public JuliaSet() {
        c = new Complex();
    }

    public JuliaSet(Complex complex) {
        this.c = complex;
    }

    @Override
    public Complex getZ0(ComplexPlane plane, double x, double y, double originX, double originY, double offsetX, double offsetY) {
        return new Complex(originX + plane.getStep() * (x - offsetX), originY - plane.getStep() * (y - offsetY));
    }

    @Override
    public Function<Complex, Complex> getF(ComplexPlane plane, double x, double y, double originX, double originY, double offsetX, double offsetY) {
        return (Complex z) -> z.multiply(z).add(c);
    }
}
