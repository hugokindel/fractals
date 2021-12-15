package com.ustudents.fgen.handlers.image;

import java.awt.image.BufferedImage;

public abstract class ImageHandler {
    public abstract BufferedImage fillImage(int[][] divergenceIndexes, int maxIterations);
}
