package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.CalculationHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SingleImageGenerator extends SingleMemoryGenerator {
    public BufferedImage bufferedImage;

    public SingleImageGenerator(CalculationHandler handler) {
        super(handler);
    }

    @Override
    public void generate(int width, int height) {
        super.generate(width, height);

        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0 ; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = divergenceIndexes[y][x];
                int color = Color.HSBtoRGB((float)index / handler.maxIterations, 0.7f, 0.7f);
                bufferedImage.setRGB(x, y, color);
            }
        }
    }
}
