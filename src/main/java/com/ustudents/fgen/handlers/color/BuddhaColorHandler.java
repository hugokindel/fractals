package com.ustudents.fgen.handlers.color;

import com.ustudents.fgen.common.json.JsonSerializable;

/** Color Handler to be used to generate Buddha fractal. */
@JsonSerializable(serializeClassName = true)
public class BuddhaColorHandler implements ColorHandler {
    public int computeColor(int indexT, int maxIterations) {
        if (indexT == 0) {
            return (0 << 16) | (0 << 8) | 0;
        } else {
            int red = ((indexT % 256) << 16) * 3 + ((indexT % 256) << 16) / 4;
            int green = (((indexT + 85) % 256) << 8) * 3 + (((indexT + 85) % 256) << 8) / 4;
            int blue = ((indexT + 170) % 256) * 3 + ((indexT + 170) % 256) / 4;
            return (red | green | blue) * 3 + (red | green | blue) / 4;
        }
    }
}