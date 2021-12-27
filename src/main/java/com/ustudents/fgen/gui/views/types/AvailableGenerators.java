package com.ustudents.fgen.gui.views.types;

import com.ustudents.fgen.generators.Generator;
import com.ustudents.fgen.generators.JpegGenerator;
import com.ustudents.fgen.generators.PngGenerator;
import com.ustudents.fgen.generators.SingleImageGenerator;

public enum AvailableGenerators {
    JPEG("JPEG"),
    PNG("PNG");

    private final String name;

    AvailableGenerators(String type) {
        this.name = type;
    }

    public static AvailableGenerators fromGenerator(Generator generator) {
        if (generator instanceof PngGenerator) {
            return AvailableGenerators.PNG;
        }

        return AvailableGenerators.JPEG;
    }

    public SingleImageGenerator toGenerator(SingleImageGenerator oldGenerator) {
        SingleImageGenerator generator;

        if (this == PNG) {
            generator = new PngGenerator();
        } else {
            generator = new JpegGenerator();
        }

        generator.name = oldGenerator.name;
        generator.width = oldGenerator.width;
        generator.height = oldGenerator.height;
        generator.offsetX = oldGenerator.offsetX;
        generator.offsetY = oldGenerator.offsetY;
        generator.calculationHandler = oldGenerator.calculationHandler;
        generator.divergenceIndexes = oldGenerator.divergenceIndexes;
        generator.bufferedImage = oldGenerator.bufferedImage;
        generator.aliasingType = oldGenerator.aliasingType;
        generator.imageHandler = oldGenerator.imageHandler;

        String path;

        if (oldGenerator instanceof PngGenerator) {
            path = ((PngGenerator)oldGenerator).path;
        } else {
            path = ((JpegGenerator)oldGenerator).path;
        }

        if (generator instanceof PngGenerator) {
            ((PngGenerator)generator).path = path;
        } else {
            ((JpegGenerator)generator).path = path;
        }

        return generator;
    }

    @Override
    public String toString() {
        return name;
    }
}
