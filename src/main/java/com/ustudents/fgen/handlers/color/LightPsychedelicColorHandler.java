package com.ustudents.fgen.handlers.color;

import com.ustudents.fgen.common.json.JsonSerializable;

/** Stylized color handler to generate a kind of chaotic light color scheme. */
@JsonSerializable(serializeClassName = true)
public class LightPsychedelicColorHandler implements ColorHandler {
    public int computeColor(int index, int maxIterations) {
        int red = ((index % 256) << 16) * 3 + ((index % 256) << 16) / 4;
        int green = (((index + 85) % 256) << 8) * 3 + (((index + 85) % 256) << 8) / 4;
        int blue = ((index + 170) % 256) * 3 + ((index + 170) % 256) / 4;
        return (red | green | blue) * 3 + (red | green | blue) / 4;
    }
}
