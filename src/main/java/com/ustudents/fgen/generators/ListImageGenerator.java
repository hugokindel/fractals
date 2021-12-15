package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.CalculationHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ListImageGenerator extends ListMemoryGenerator {
    BufferedImage[] bufferedImages;

    @Override
    public void generate(int width, int height) {
        super.generate(width, height);

        bufferedImages = new BufferedImage[handlers.size()];

        for (int i = 0; i < bufferedImages.length; i++) {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int y = 0 ; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int index = divergenceIndexesPerHandler[i][y][x];
                    int color = Color.HSBtoRGB((float)index / handlers.get(i).maxIterations, 0.7f, 0.7f);
                    image.setRGB(x, y, color);
                }
            }

            bufferedImages[i] = image;
        }
    }
}
