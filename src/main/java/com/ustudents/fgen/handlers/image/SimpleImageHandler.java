package com.ustudents.fgen.handlers.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SimpleImageHandler extends ImageHandler {
    @Override
    public BufferedImage fillImage(int[][] divergenceIndexes, int maxIterations) {
        BufferedImage bufferedImage = new BufferedImage(divergenceIndexes.length, divergenceIndexes[0].length, BufferedImage.TYPE_INT_RGB);

        for (int y = 0 ; y < divergenceIndexes.length; y++) {
            for (int x = 0; x < divergenceIndexes[0].length; x++) {
                int index = divergenceIndexes[y][x];
                int color = Color.HSBtoRGB((float)index / maxIterations, 0.7f, 0.7f);
                bufferedImage.setRGB(x, y, color);
            }
        }

        return bufferedImage;
    }
}
