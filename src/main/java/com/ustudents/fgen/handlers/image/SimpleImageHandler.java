package com.ustudents.fgen.handlers.image;

import com.ustudents.fgen.handlers.color.ColorHandler;

import java.awt.image.BufferedImage;

public class SimpleImageHandler extends ImageHandler {
    public SimpleImageHandler(ColorHandler colorGenerator) {
        super(colorGenerator);
    }

    @Override
    public BufferedImage fillImage(int[][] divergenceIndexes, int maxIterations) {
        BufferedImage bufferedImage = new BufferedImage(divergenceIndexes.length, divergenceIndexes[0].length, BufferedImage.TYPE_INT_RGB);

        for (int y = 0 ; y < divergenceIndexes.length; y++) {
            for (int x = 0; x < divergenceIndexes[0].length; x++) {
                computeColorOfIndex(bufferedImage, x, y, divergenceIndexes[y][x], maxIterations);
            }
        }

        return bufferedImage;
    }
}
