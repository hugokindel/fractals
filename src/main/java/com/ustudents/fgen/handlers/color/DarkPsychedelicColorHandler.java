package com.ustudents.fgen.handlers.color;

import com.ustudents.fgen.common.json.JsonSerializable;

/** Stylized color handler to generate a kind of chaotic dark color scheme. */
@JsonSerializable(serializeClassName = true)
public class DarkPsychedelicColorHandler implements ColorHandler {
    public int computeColor(int index, int maxIterations) {
        int color = ((index % 256) << 16) | (((index + 85) % 256) << 8) | ((index + 170) % 256);
        return color * 3 + color / 4;
    }
}
