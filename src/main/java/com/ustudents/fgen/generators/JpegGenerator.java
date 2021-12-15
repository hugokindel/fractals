package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.CalculationHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class JpegGenerator extends SingleImageGenerator {
    public String path;

    public JpegGenerator(CalculationHandler handler, String path) {
        super(handler);
        this.path = path;
    }

    @Override
    public void generate(int width, int height) {
        super.generate(width, height);

        File file = new File(path);

        try {
            ImageIO.write(image, "jpeg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
