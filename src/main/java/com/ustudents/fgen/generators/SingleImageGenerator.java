package com.ustudents.fgen.generators;

import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.common.json.JsonSerializableType;
import com.ustudents.fgen.common.utils.AliasingType;
import com.ustudents.fgen.handlers.calculation.CalculationHandler;
import com.ustudents.fgen.handlers.image.ImageHandler;

import java.awt.image.BufferedImage;
import java.util.Map;

@JsonSerializable
@SuppressWarnings("unchecked")
public class SingleImageGenerator extends SingleMemoryGenerator {
    public BufferedImage bufferedImage = null;

    @JsonSerializable(type = JsonSerializableType.SerializableOnly)
    public AliasingType aliasingType = AliasingType.X1;

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

        int n = aliasingType.getMultiplier();
        calculationHandler.plane.setStep(calculationHandler.plane.getStep() / n);
        width *= n;
        height *= n;
        offsetX *= n;
        offsetY *= n;

        super.generate();

        calculationHandler.plane.setStep(oldStep);
        width = oldWidth;
        height = oldHeight;
        offsetX = oldOffsetX;
        offsetY = oldOffsetY;

        bufferedImage = imageHandler.fillImage(divergenceIndexes, calculationHandler.maxIterations, aliasingType);
    }
}
