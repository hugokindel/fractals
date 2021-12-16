package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.calculation.CalculationHandler;
import com.ustudents.fgen.handlers.image.ImageHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PngGenerator extends SingleImageGenerator {
    public String path;

    public PngGenerator(CalculationHandler calculationHandler, ImageHandler imageHandler, String path) {
        super(calculationHandler, imageHandler);
        this.path = path;
    }

    @Override
    public void generate(int width, int height, double offsetX, double offsetY) {
        super.generate(width, height, offsetX, offsetY);

        File file = new File(path);

        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
