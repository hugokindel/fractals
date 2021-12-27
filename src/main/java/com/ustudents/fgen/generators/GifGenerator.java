package com.ustudents.fgen.generators;

import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.utils.GifSequenceWriter;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 Generates a gif of fractal generators.
 Can currently be used in a config file through the CLI, but not available within the GUI.
 */
@JsonSerializable
public class GifGenerator extends ListImageGenerator {
    @JsonSerializable
    public String path;

    @JsonSerializable(necessary = false)
    public Integer msBetweenFrames = 100;

    @JsonSerializable(necessary = false)
    public Boolean loopContinuously = true;

    public GifGenerator() {

    }

    public GifGenerator(String path, int msBetweenFrames, boolean loopContinuously) {
        super();
        this.path = path;
        this.msBetweenFrames = msBetweenFrames;
        this.loopContinuously = loopContinuously;
    }

    public void generate() {
        super.generate();

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
