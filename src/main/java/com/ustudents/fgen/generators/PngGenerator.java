package com.ustudents.fgen.generators;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.handlers.calculation.CalculationHandler;
import com.ustudents.fgen.handlers.image.ImageHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/** Generates a Png from a fractal. */
@JsonSerializable(serializeClassName = true)
public class PngGenerator extends SingleImageGenerator {
    @JsonSerializable
    public String path = null;

    public PngGenerator() {

    }

    public PngGenerator(int width, int height, double offsetX, double offsetY, CalculationHandler calculationHandler, ImageHandler imageHandler, String path) {
        super(width, height, offsetX, offsetY, calculationHandler, imageHandler);
        this.path = path;
    }

    @Override
    public void generate() {
        super.generate();

        File file = new File(path);

        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
