package com.ustudents.fgen.handlers.color;

import com.ustudents.fgen.common.json.JsonSerializable;

@JsonSerializable(serializeClassName = true)
public class RedColorHandler implements ColorHandler {
    public int computeColor(int index,int maxIterations ) {
        int r = (255 * index) / maxIterations;
        int g = 0;
        int b = 0;
        return (r << 16) | (g << 8) | b;
    }
}
