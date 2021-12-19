package com.ustudents.fgen.handlers.color;

import com.ustudents.fgen.common.json.JsonSerializable;

import java.awt.*;

@JsonSerializable(serializeClassName = true)
public class HsbColorHandler implements ColorHandler {
    public int computeColor(int index, int maxIterations) {
        return Color.HSBtoRGB((float)index / maxIterations, 0.7f, 0.7f);
    }
}
