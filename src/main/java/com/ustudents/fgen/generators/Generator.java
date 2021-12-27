package com.ustudents.fgen.generators;

import com.ustudents.fgen.common.json.JsonSerializable;

import java.util.UUID;

/** Defines a generator, a list of data that can be used to generate a fractal. */
@JsonSerializable(serializeClassName = true)
public abstract class Generator {
    @JsonSerializable(necessary = false)
    public String name = "Default";

    @JsonSerializable
    public Integer width = 0;

    @JsonSerializable
    public Integer height = 0;

    @JsonSerializable
    public Double offsetX = 0.;

    @JsonSerializable
    public Double offsetY = 0.;

    public Generator() {

    }

    public Generator(int width, int height, double offsetX, double offsetY) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public abstract void generate();

    @Override
    public String toString() {
        return name;
    }
}
