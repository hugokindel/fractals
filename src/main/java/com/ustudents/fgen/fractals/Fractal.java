package com.ustudents.fgen.fractals;

import java.util.function.Function;

public abstract class Fractal {
    public abstract Complex getZ0(ComplexPlane plane, double x, double y, double originX, double originY);

    public abstract Function<Complex, Complex> getF();
}
