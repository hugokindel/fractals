package com.ustudents.fgen.fractals;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

@JsonSerializable(serializeClassName = true)
public class MandelbrotSet extends JuliaSet {
    public MandelbrotSet() {

    }

    @Override
    public Complex getZ0(ComplexPlane plane, double x, double y, double originX, double originY, double offsetX, double offsetY) {
        complex = super.getZ0(plane, x, y, originX, originY, offsetX, offsetY);
        return new Complex(0, 0);
    }
}
