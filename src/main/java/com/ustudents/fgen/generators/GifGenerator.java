package com.ustudents.fgen.generators;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.benchmark.Benchmark;
import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.common.utils.GifSequenceWriter;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;

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

            Benchmark benchmark = new Benchmark();
            for (BufferedImage bufferedImage : bufferedImages) {
                writer.writeToSequence(bufferedImage);
            }
            FGen.gifCreation = FGen.gifCreation.plus(benchmark.end());

            writer.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
