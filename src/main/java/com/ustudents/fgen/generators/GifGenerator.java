package com.ustudents.fgen.generators;

import com.ustudents.fgen.common.utils.GifSequenceWriter;
import com.ustudents.fgen.handlers.CalculationHandler;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GifGenerator extends Generator {
    public List<CalculationHandler> handlers;
    public int width;
    public int height;
    public String path;
    public int msBetweenFrames;
    public boolean loopContinuously;

    public GifGenerator(int width, int height, String path, int msBetweenFrames, boolean loopContinuously) {
        this.handlers = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.path = path;
        this.msBetweenFrames = msBetweenFrames;
        this.loopContinuously = loopContinuously;
    }

    public void addCalculationHandler(CalculationHandler handler) {
        handlers.add(handler);
    }

    public void generate() {
        if (handlers.size() == 0) {
            return;
        }

        BufferedImage[] bufferedImages = new BufferedImage[handlers.size()];

        for (int i = 0; i < bufferedImages.length; i++) {
            CalculationHandler handler = handlers.get(i);
            int[][] divergenceIndexes = handler.calculateDivergenceIndexes(width, height);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int y = 0 ; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int index = divergenceIndexes[y][x];
                    int color = Color.HSBtoRGB((float)index / handler.maxIterations, 0.7f, 0.7f);
                    image.setRGB(x, y, color);
                }
            }

            bufferedImages[i] = image;
        }

        try {
            ImageOutputStream output = new FileImageOutputStream(new File(path));
            GifSequenceWriter writer = new GifSequenceWriter(output, bufferedImages[0].getType(), msBetweenFrames, loopContinuously);

            for (BufferedImage bufferedImage : bufferedImages) {
                writer.writeToSequence(bufferedImage);
            }

            writer.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
