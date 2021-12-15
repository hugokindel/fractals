package com.ustudents.fgen.generators;

import com.ustudents.fgen.common.utils.GifSequenceWriter;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;

public class GifGenerator extends ListImageGenerator {
    public String path;
    public int msBetweenFrames;
    public boolean loopContinuously;

    public GifGenerator(String path, int msBetweenFrames, boolean loopContinuously) {
        super();
        this.path = path;
        this.msBetweenFrames = msBetweenFrames;
        this.loopContinuously = loopContinuously;
    }

    public void generate(int width, int height) {
        super.generate(width, height);

        if (bufferedImages.length == 0) {
            return;
        }

        try {
            ImageOutputStream output = new FileImageOutputStream(new File(path));
            GifSequenceWriter writer = new GifSequenceWriter(output, bufferedImages[0], msBetweenFrames, loopContinuously);

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
