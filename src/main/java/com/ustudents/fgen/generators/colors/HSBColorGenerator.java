package com.ustudents.fgen.generators.colors;

import java.awt.*;

public class HSBColorGenerator implements ColorGenerator {
    public int getColor(int index,int maxIterations){
        return Color.HSBtoRGB((float)index / maxIterations, 0.7f, 0.7f);
    }
}
