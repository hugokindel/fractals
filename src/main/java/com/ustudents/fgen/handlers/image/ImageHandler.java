package com.ustudents.fgen.handlers.image;

import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.common.json.JsonSerializableType;
import com.ustudents.fgen.handlers.color.ColorHandler;

import java.awt.image.BufferedImage;
import java.util.Map;

@JsonSerializable(serializeClassName = true)
@SuppressWarnings("unchecked")
public abstract class ImageHandler {
    @JsonSerializable(type = JsonSerializableType.SerializableOnly)
    public ColorHandler colorHandler = null;

    public ImageHandler() {

    }

    public ImageHandler(ColorHandler colorHandler) {
        this.colorHandler = colorHandler;
    }

    @JsonSerializableConstructor
    public void deserialize(Map<String, Object> elements) {
        Map<String, Object> colorHandlerMap = (Map<String, Object>)elements.get("colorHandler");
        try {
            Class<ColorHandler> colorHandlerClass =
                    (Class<ColorHandler>)Class.forName("com.ustudents.fgen.handlers.color." + colorHandlerMap.get("class"));
            colorHandlerMap.remove("class");
            colorHandler = Json.deserialize(colorHandlerMap, colorHandlerClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract BufferedImage fillImage(int[][] divergenceIndexes, int maxIterations);

    protected void computeColorOfIndex(BufferedImage bufferedImage, int x, int y, int index, int maxIterations) {
        int color = colorHandler.computeColor(index,maxIterations);
        bufferedImage.setRGB(x, y, color);
    }
}
