package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.CalculationHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PngGenerator extends BufferedImageGenerator {
    public String path;

    public PngGenerator(CalculationHandler handler, int width, int height, String path) {
        super(handler, width, height);
        this.path = path;
    }

    @Override
    public void generate() {
        super.generate();

        File file = new File(path);

        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}