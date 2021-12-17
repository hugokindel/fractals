package com.ustudents.fgen.handlers.image;

import com.ustudents.fgen.handlers.color.ColorHandler;

import java.awt.image.BufferedImage;

public abstract class ImageHandler {
    public ColorHandler colorGenerator;

    public ImageHandler(ColorHandler colorGenerator) {
        this.colorGenerator = colorGenerator;
    }

    public abstract BufferedImage fillImage(int[][] divergenceIndexes, int maxIterations);

    protected void computeColorOfIndex(BufferedImage bufferedImage, int x, int y, int index, int maxIterations) {
        int color = colorGenerator.computeColor(index,maxIterations);
        bufferedImage.setRGB(x, y, color);
    }
}
