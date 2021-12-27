package com.ustudents.fgen.maths;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;

@JsonSerializable
public class ComplexPlane {
    @JsonSerializable
    public Complex start = null;

    @JsonSerializable
    public Complex end = null;

    @JsonSerializable
    public Double step = 0.;

    private int viewWidth;

    private int viewHeight;

    public ComplexPlane() {

    }

    public ComplexPlane(Complex start, Complex end, double step) {
        this.start = start;
        this.end = end;
        this.step = step;

        computeViewSize();
    }

    @JsonSerializableConstructor
    public void deserialize() {
        computeViewSize();
    }

    public Complex getStart() {
        return start;
    }

    public Complex getEnd() {
        return end;
    }

    public double getStep() {
        return step;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public double getOriginX(int width) {
        return -((double)width / viewWidth);
    }

    public double getOriginY(int height) {
        return ((double)height / viewHeight);
    }

    public void setStart(Complex start) {
        this.start = start;

        computeViewSize();
    }

    public void setEnd(Complex end) {
        this.end = end;

        computeViewSize();
    }

    public void setStep(double step) {
        this.step = step;

        computeViewSize();
    }

    private void computeViewSize() {
        this.viewWidth = (int)(Math.abs(end.real - start.real) / step);
        this.viewHeight = (int)(Math.abs(end.imaginary - start.imaginary) / step);
    }
}
