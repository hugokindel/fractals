package com.ustudents.fgen.generators;

import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.common.json.JsonSerializableType;
import com.ustudents.fgen.handlers.calculation.CalculationHandler;
import com.ustudents.fgen.handlers.color.BasicColorHandler;
import com.ustudents.fgen.handlers.image.ImageHandler;
import com.ustudents.fgen.handlers.image.SimpleImageHandler;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonSerializable
@SuppressWarnings("unchecked")
public class ListImageGenerator extends ListGenerator {
    BufferedImage[] bufferedImages;

    @JsonSerializable(type = JsonSerializableType.SerializableOnly)
    List<ImageHandler> imageHandlers = new ArrayList<>();

    @Override
    public void addCalculationHandler(CalculationHandler calculationHandler) {
        calculationHandlers.add(calculationHandler);
        imageHandlers.add(new SimpleImageHandler(new BasicColorHandler()));
    }

    public void addCalculationHandler(CalculationHandler calculationHandler, ImageHandler imageHandler) {
        calculationHandlers.add(calculationHandler);
        imageHandlers.add(imageHandler);
    }

    @Override
    public void generate() {
        bufferedImages = new BufferedImage[calculationHandlers.size()];

        for (int i = 0; i < calculationHandlers.size(); i++) {
            SingleImageGenerator generator = new SingleImageGenerator(width, height, offsetX, offsetY, calculationHandlers.get(i), imageHandlers.get(i));
            generator.generate();
            bufferedImages[i] = generator.bufferedImage;
        }
    }

    @JsonSerializableConstructor
    public void deserialize(Map<String, Object> elements) {
        super.deserialize(elements);

        for (Map<String, Object> imageHandlerMap : (List<Map<String, Object>>)elements.get("imageHandlers")) {
            try {
                Class<ImageHandler> imageHandlerClass =
                        (Class<ImageHandler>)Class.forName("com.ustudents.fgen.handlers.image." + imageHandlerMap.get("class"));
                imageHandlerMap.remove("class");
                imageHandlers.add(Json.deserialize(imageHandlerMap, imageHandlerClass));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
