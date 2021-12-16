package com.ustudents.fgen.generators.colors;

import java.awt.*;

public class RedColorGenerator {
    public int getColor(int index,int maxIterations){
        int r = (255*index)/maxIterations;
        int g = 0;
        int b = 0;
        return (r << 16) | (g << 8) | b;
    }
}
