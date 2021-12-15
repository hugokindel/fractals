package com.ustudents.fgen.generators;

import java.awt.image.BufferedImage;

public class ListImageGenerator extends ListGenerator {
    BufferedImage[] bufferedImages;

    @Override
    public void generate(int width, int height) {
        bufferedImages = new BufferedImage[handlers.size()];

        for (int i = 0; i < handlers.size(); i++) {
            SingleImageGenerator generator = new SingleImageGenerator(handlers.get(i));
            generator.generate(width, height);
            bufferedImages[i] = generator.bufferedImage;
        }
    }
}
