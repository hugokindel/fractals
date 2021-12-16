package com.ustudents.fgen.generators.colors;

public class FullColorGenerator implements ColorGenerator {
    public int getColor(int index,int maxIterations){
        return ((index % 256) << 16) | (((index + 85) % 256) << 8) | ((index + 170) % 256);
    }
}
