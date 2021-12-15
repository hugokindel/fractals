package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.CalculationHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PngGenerator extends SingleImageGenerator {
    public String path;

    public PngGenerator(CalculationHandler handler, String path) {
        super(handler);
        this.path = path;
    }

    @Override
    public void generate(int width, int height) {
        super.generate(width, height);

        File file = new File(path);

        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
