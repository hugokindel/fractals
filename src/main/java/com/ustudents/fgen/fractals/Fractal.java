package com.ustudents.fgen.fractals;

import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

import java.util.function.Function;

public abstract class Fractal {
    public abstract Complex getZ0(ComplexPlane plane, double x, double y, double originX, double originY, double offsetX, double offsetY);

    public abstract Function<Complex, Complex> getF();
}
