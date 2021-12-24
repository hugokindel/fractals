package com.ustudents.fgen.handlers.color;

import com.ustudents.fgen.common.json.JsonSerializable;

@JsonSerializable(serializeClassName = true)
public class BasicColorHandler implements ColorHandler {
    public int computeColor(int index, int maxIterations) {
        return ((index % 256) << 16) | (((index + 85) % 256) << 8) | ((index + 170) % 256);
    }
}
