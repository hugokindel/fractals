package com.ustudents.fgen.handlers.image;

import com.ustudents.fgen.generators.colors.ColorGenerator;
import com.ustudents.fgen.generators.colors.FullColorGenerator;

import java.awt.image.BufferedImage;

public abstract class ImageHandler {
    private ColorGenerator colorGenerator = new FullColorGenerator();

    public abstract BufferedImage fillImage(int[][] divergenceIndexes, int maxIterations);

    protected void computeColor(BufferedImage bufferedImage, int x, int y, int index, int maxIterations) {
        int color = colorGenerator.getColor(index,maxIterations);
        bufferedImage.setRGB(x, y, color);
    }

    public void setColorGenerator(ColorGenerator cg){
        colorGenerator = cg;
    }
}
