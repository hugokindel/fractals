package com.ustudents.fgen.fractals;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;
import com.ustudents.fgen.maths.PolynomialFunctionParser;
import com.ustudents.fgen.maths.PolynomialFunctionValues;

import java.util.function.Function;

@JsonSerializable
public class PolynomialFunction extends Fractal {
    @JsonSerializable
    public String f;

    @JsonSerializable
    public Complex c;

    public Function<PolynomialFunctionValues, Complex> function;

    @JsonSerializableConstructor
    public void deserialize() {
        function = PolynomialFunctionParser.parse(f);
    }

    @Override
    public Complex getZ0(ComplexPlane plane, double x, double y, double originX, double originY, double offsetX, double offsetY) {
        return new Complex(originX + plane.getStep() * (x - offsetX), originY - plane.getStep() * (y - offsetY));
    }

    @Override
    public Function<Complex, Complex> getF(ComplexPlane plane, double x, double y, double originX, double originY, double offsetX, double offsetY) {
        return z -> function.apply(new PolynomialFunctionValues(z, c));
    }
}
