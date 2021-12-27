package com.ustudents.fgen.gui.views.types;

import com.ustudents.fgen.format.AliasingType;
import com.ustudents.fgen.fractals.Fractal;
import com.ustudents.fgen.fractals.JuliaSet;
import com.ustudents.fgen.fractals.MandelbrotSet;
import com.ustudents.fgen.maths.Complex;

public enum AvailableFractals {
    JULIA("Julia"),
    MANDELBROT("Mandelbrot");

    private final String name;

    AvailableFractals(String type) {
        this.name = type;
    }

    public static AvailableFractals fromFractal(Fractal fractal) {
        if (fractal instanceof JuliaSet) {
            return JULIA;
        }

        return MANDELBROT;
    }

    public Fractal toFractal() {
        switch (this) {
            case JULIA -> {
                return new JuliaSet(new Complex(-0.7269, 0.1889));
            }
        }

        return new MandelbrotSet();
    }

    @Override
    public String toString() {
        return name;
    }
}