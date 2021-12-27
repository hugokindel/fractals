package com.ustudents.fgen.handlers.calculation;

import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.common.json.JsonSerializableType;
import com.ustudents.fgen.fractals.Fractal;
import com.ustudents.fgen.fractals.MandelbrotSet;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

import java.util.Map;
import java.util.function.Function;

@JsonSerializable(serializeClassName = true)
@SuppressWarnings("unchecked")
public abstract class CalculationHandler {
    @JsonSerializable(type = JsonSerializableType.SerializableOnly)
    public Fractal fractal = new MandelbrotSet();

    @JsonSerializable(necessary = false)
    public ComplexPlane plane = new ComplexPlane(new Complex(-1, 1), new Complex(1, -1), 0.001);

    @JsonSerializable(necessary = false)
    public Integer maxIterations = 1000;

    @JsonSerializable(necessary = false)
    public Double radius = 2.;

    public CalculationHandler() {

    }

    public CalculationHandler(Fractal fractal, ComplexPlane plane, int maxIterations, double radius) {
        this.fractal = fractal;
        this.plane = plane;
        this.maxIterations = maxIterations;
        this.radius = radius;
    }

    @JsonSerializableConstructor
    public void deserialize(Map<String, Object> elements) {
        Map<String, Object> fractalMap = (Map<String, Object>)elements.get("fractal");
        try {
            Class<Fractal> fractalClass =
                    (Class<Fractal>)Class.forName("com.ustudents.fgen.fractals." + fractalMap.get("class"));
            fractalMap.remove("class");
            fractal = Json.deserialize(fractalMap, fractalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract int[][] computeDivergenceIndexes(int width, int height, double offsetX, double offsetY);

    protected int computeDivergenceIndex(Complex z0, Function<Complex, Complex> f) {
        int n = 0;
        Complex zn = z0.clone();

        while (zn.getModulus() <= radius && n < maxIterations) {
            zn = f.apply(zn);
            n++;
        }

        return n;
    }
}
