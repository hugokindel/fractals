package com.ustudents.fgen.generators;

import com.ustudents.fgen.FGen;
import com.ustudents.fgen.common.benchmark.Benchmark;
import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.common.json.JsonSerializableType;
import com.ustudents.fgen.format.AliasingType;
import com.ustudents.fgen.handlers.calculation.CalculationHandler;
import com.ustudents.fgen.handlers.image.ImageHandler;

import java.awt.image.BufferedImage;
import java.util.Map;

@JsonSerializable
@SuppressWarnings("unchecked")
public class SingleImageGenerator extends SingleMemoryGenerator {
    public BufferedImage bufferedImage = null;

    @JsonSerializable(type = JsonSerializableType.SerializableOnly)
    public AliasingType aliasingType = AliasingType.x1;

    @JsonSerializable(type = JsonSerializableType.SerializableOnly)
    public ImageHandler imageHandler = null;

    public SingleImageGenerator() {

    }

    public SingleImageGenerator(int width, int height, double offsetX, double offsetY, CalculationHandler calculationHandler, ImageHandler imageHandler) {
        super(width, height, offsetX, offsetY, calculationHandler);
        this.imageHandler = imageHandler;
    }

    @JsonSerializableConstructor
    @Override
    public void deserialize(Map<String, Object> elements) {
        super.deserialize(elements);

        Map<String, Object> imageHandlerMap = (Map<String, Object>)elements.get("imageHandler");

        if (elements.containsKey("aliasingType")) {
            aliasingType = AliasingType.valueOf((String)elements.get("aliasingType"));
        }

        try {
            Class<ImageHandler> imageHandlerClass =
                    (Class<ImageHandler>)Class.forName("com.ustudents.fgen.handlers.image." + imageHandlerMap.get("class"));
            imageHandlerMap.remove("class");
            imageHandler = Json.deserialize(imageHandlerMap, imageHandlerClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generate() {
        double oldStep = calculationHandler.plane.getStep();
        int oldWidth = width;
        int oldHeight = height;
        double oldOffsetX = offsetX;
        double oldOffsetY = offsetY;

        if (aliasingType == AliasingType.x2) {
            calculationHandler.plane.setStep(calculationHandler.plane.getStep() / 2);
            width *= 2;
            height *= 2;
            offsetX *= 2;
            offsetY *= 2;
        } else if (aliasingType == AliasingType.x4) {
            calculationHandler.plane.setStep(calculationHandler.plane.getStep() / 4);
            width *= 4;
            height *= 4;
            offsetX *= 4;
            offsetY *= 4;
        }

        super.generate();

        calculationHandler.plane.setStep(oldStep);
        width = oldWidth;
        height = oldHeight;
        offsetX = oldOffsetX;
        offsetY = oldOffsetY;

        Benchmark benchmark = new Benchmark();
        bufferedImage = imageHandler.fillImage(divergenceIndexes, calculationHandler.maxIterations, aliasingType);
        FGen.imageHandlerDuration = FGen.imageHandlerDuration.plus(benchmark.end());
    }
}
