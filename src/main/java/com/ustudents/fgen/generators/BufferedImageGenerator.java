package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.CalculationHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImageGenerator extends MemoryGenerator {
    protected BufferedImage image;

    public BufferedImageGenerator(CalculationHandler handler, int width, int height) {
        super(handler, width, height);
    }

    @Override
    public void generate() {
        super.generate();

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0 ; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = divergenceIndexes[y][x];
                int color = Color.HSBtoRGB((float)index / handler.maxIterations, 0.7f, 0.7f);
                image.setRGB(x, y, color);
            }
        }
    }
}
