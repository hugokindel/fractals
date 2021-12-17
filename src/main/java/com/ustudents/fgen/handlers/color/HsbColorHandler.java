package com.ustudents.fgen.handlers.color;

import java.awt.*;

public class HsbColorHandler implements ColorHandler {
    public int computeColor(int index, int maxIterations) {
        return Color.HSBtoRGB((float)index / maxIterations, 0.7f, 0.7f);
    }
}
