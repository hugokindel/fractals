package com.ustudents.fgen.handlers.image;

import java.awt.image.BufferedImage;

public abstract class ImageHandler {
    public abstract BufferedImage fillImage(int[][] divergenceIndexes, int maxIterations);

    protected void computeColor(BufferedImage bufferedImage, int x, int y, int index, int maxIterations) {
        //int color = Color.HSBtoRGB((float)index / maxIterations, 0.7f, 0.7f);
        int color = ((index % 256) << 16) | (((index + 85) % 256) << 8) | ((index + 170) % 256);
        bufferedImage.setRGB(x, y, color);
    }
}
