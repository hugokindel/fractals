package com.ustudents.fgen.fractals;

public class ComplexPlane {
    private Complex start;

    private Complex end;

    private double zoom;

    private int viewWidth;

    private int viewHeight;

    public ComplexPlane(Complex start, Complex end, double zoom) {
        this.start = start;
        this.end = end;
        this.zoom = zoom;

        calculateViewSize();
    }

    public Complex getStart() {
        return start;
    }

    public Complex getEnd() {
        return end;
    }

    public double getZoom() {
        return zoom;
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

        calculateViewSize();
    }

    public void setEnd(Complex end) {
        this.end = end;

        calculateViewSize();
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;

        calculateViewSize();
    }

    private void calculateViewSize() {
        this.viewWidth = (int)(Math.abs(end.real - start.real) / zoom);
        this.viewHeight = (int)(Math.abs(end.imaginary - start.imaginary) / zoom);
    }
}
