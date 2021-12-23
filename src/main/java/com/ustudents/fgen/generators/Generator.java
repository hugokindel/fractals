package com.ustudents.fgen.generators;

import com.ustudents.fgen.common.json.JsonSerializable;

import java.util.UUID;

@JsonSerializable(serializeClassName = true)
public abstract class Generator {
    private UUID id = null;

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

    public UUID getId() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
