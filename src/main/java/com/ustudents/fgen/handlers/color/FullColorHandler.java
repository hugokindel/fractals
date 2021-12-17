package com.ustudents.fgen.handlers.color;

public class FullColorHandler implements ColorHandler {
    public int computeColor(int index, int maxIterations) {
        return ((index % 256) << 16) | (((index + 85) % 256) << 8) | ((index + 170) % 256);
    }
}
