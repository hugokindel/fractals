package com.ustudents.fgen.handlers.image;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.utils.AliasingType;
import com.ustudents.fgen.handlers.color.ColorHandler;

import java.awt.image.BufferedImage;

/** Compute color per indexes using a single thread. */
@JsonSerializable(serializeClassName = true)
public class SimpleImageHandler extends ImageHandler {
    public SimpleImageHandler() {

    }

    public SimpleImageHandler(ColorHandler colorHandler) {
        super(colorHandler);
    }

    @Override
    public BufferedImage fillImage(int[][] divergenceIndexes, int maxIterations, AliasingType aliasingType) {
        BufferedImage bufferedImage = createImage(divergenceIndexes, aliasingType);

        for (int y = 0 ; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                computeColorOfIndex(bufferedImage, x, y, divergenceIndexes, maxIterations, aliasingType);
            }
        }

        return bufferedImage;
    }
}
