package com.ustudents.fgen.fractals;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PngGenerator extends Generator {
    public int width;
    public int height;
    public String path;

    public PngGenerator(CalculationHandler handler, int width, int height, String path) {
        super(handler);
        this.width = width;
        this.height = height;
        this.path = path;
    }

    @Override
    public void generate() {
        int[][] divergenceIndexes = handler.calculateDivergenceIndexes(width, height);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0 ; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = divergenceIndexes[y][x];
                int color = Color.HSBtoRGB((float)index / handler.getMaxIterations(), 0.7f, 0.7f);
                image.setRGB(x, y, color);
            }
        }

        File file = new File(path);

        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
