package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.calculation.CalculationHandler;
import com.ustudents.fgen.handlers.image.ImageHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class JpegGenerator extends SingleImageGenerator {
    public String path;

    public JpegGenerator(CalculationHandler calculationHandler, ImageHandler imageHandler, String path) {
        super(calculationHandler, imageHandler);
        this.path = path;
    }

    @Override
    public void generate(int width, int height) {
        super.generate(width, height);

        File file = new File(path);

        try {
            ImageIO.write(bufferedImage, "jpeg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
