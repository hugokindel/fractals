package com.ustudents.fgen.fractals;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

import java.util.function.Function;

@JsonSerializable(serializeClassName = true)
public class JuliaSet extends Fractal {
    @JsonSerializable(necessary = false)
    protected Complex complex = new Complex();

    public JuliaSet() {

    }

    public JuliaSet(Complex complex) {
        this.complex = complex;
    }

    @Override
    public Complex getZ0(ComplexPlane plane, double x, double y, double originX, double originY, double offsetX, double offsetY) {
        return new Complex(originX + plane.getStep() * (x - offsetX), originY - plane.getStep() * (y - offsetY));
    }

    @Override
    public Function<Complex, Complex> getF() {
        return (Complex z) -> z.multiply(z).add(complex);
    }
}
