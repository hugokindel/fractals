package com.ustudents.fgen.handlers.image;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.utils.Pool;
import com.ustudents.fgen.handlers.color.ColorHandler;

import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;

@JsonSerializable(serializeClassName = true)
public class PoolImageHandler extends ImageHandler {
    public static final int DEFAULT_PARALLELISM_THRESHOLD = 4194304;

    private class ImageTask extends RecursiveAction {
        int startIndex;
        int endIndex;
        int[][] divergenceIndexes;
        int maxIterations;
        BufferedImage bufferedImage;

        public ImageTask(int startIndex, int endIndex, int[][] divergenceIndexes, int maxIterations, BufferedImage result) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.divergenceIndexes = divergenceIndexes;
            this.maxIterations = maxIterations;
            this.bufferedImage = result;
        }

        @Override
        protected void compute() {
            int numCalculations = endIndex - startIndex;

            if (numCalculations < parallelismThreshold) {
                computeDirectly();
                return;
            }

            int middleIndex = (startIndex + endIndex) / 2;

            invokeAll(new ImageTask(startIndex, middleIndex, divergenceIndexes, maxIterations, bufferedImage),
                      new ImageTask(middleIndex, endIndex, divergenceIndexes, maxIterations, bufferedImage));
        }

        private void computeDirectly() {
            for (int i = startIndex; i < endIndex; i++) {
                int y = i / divergenceIndexes.length;
                int x = i % divergenceIndexes[0].length;
                computeColorOfIndex(bufferedImage, x, y, divergenceIndexes[y][x], maxIterations);
            }
        }
    }

    public int parallelismLevel;
    public int parallelismThreshold;

    public PoolImageHandler(ColorHandler colorHandler) {
        super(colorHandler);
        this.parallelismLevel = Runtime.getRuntime().availableProcessors();
        this.parallelismThreshold = DEFAULT_PARALLELISM_THRESHOLD;
    }

    public PoolImageHandler(ColorHandler colorHandler, int parallelismLevel) {
        this(colorHandler);
        this.parallelismLevel = parallelismLevel;
    }

    public PoolImageHandler(ColorHandler colorHandler, int parallelismLevel, int parallelismThreshold) {
        this(colorHandler, parallelismLevel);
        this.parallelismThreshold = parallelismThreshold;
    }

    @Override
    public BufferedImage fillImage(int[][] divergenceIndexes, int maxIterations) {
        BufferedImage bufferedImage = new BufferedImage(divergenceIndexes.length, divergenceIndexes[0].length, BufferedImage.TYPE_INT_RGB);
        ImageTask work = new ImageTask(0, divergenceIndexes.length * divergenceIndexes[0].length, divergenceIndexes, maxIterations, bufferedImage);

        Pool.get(parallelismLevel).invoke(work);

        return bufferedImage;
    }
}
