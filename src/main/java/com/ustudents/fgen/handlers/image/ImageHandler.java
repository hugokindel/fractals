package com.ustudents.fgen.handlers.image;

import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.common.json.JsonSerializableType;
import com.ustudents.fgen.format.AliasingType;
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

    public abstract BufferedImage fillImage(int[][] divergenceIndexes, int maxIterations, AliasingType aliasingType);

    protected BufferedImage createImage(int[][] divergenceIndexes, AliasingType aliasingType) {
        if (aliasingType == AliasingType.x1) {
            return new BufferedImage(divergenceIndexes.length, divergenceIndexes[0].length, BufferedImage.TYPE_INT_RGB);
        } else if (aliasingType == AliasingType.x2) {
            return new BufferedImage(divergenceIndexes.length / 2, divergenceIndexes[0].length / 2, BufferedImage.TYPE_INT_RGB);
        }

        return new BufferedImage(divergenceIndexes.length / 4, divergenceIndexes[0].length / 4, BufferedImage.TYPE_INT_RGB);
    }

    protected void computeColorOfIndex(BufferedImage bufferedImage, int x, int y, int[][] divergenceIndexes, int maxIterations, AliasingType aliasingType) {
        if (aliasingType == AliasingType.x1) {
            int color = colorHandler.computeColor(divergenceIndexes[y][x], maxIterations);
            bufferedImage.setRGB(x, y, color);
        } else if (aliasingType == AliasingType.x2) {
            y *= 2;
            x *= 2;
            int color1 = colorHandler.computeColor(divergenceIndexes[y][x], maxIterations);
            int color2 = colorHandler.computeColor(divergenceIndexes[y][x + 1], maxIterations);
            int color3 = colorHandler.computeColor(divergenceIndexes[y + 1][x], maxIterations);
            int color4 = colorHandler.computeColor(divergenceIndexes[y + 1][x + 1], maxIterations);
            bufferedImage.setRGB(x / 2, y / 2, color1 + color2 + color3 + color4 / 4);
        } else if (aliasingType == AliasingType.x4) {
            int color = colorHandler.computeColor(divergenceIndexes[y][x], maxIterations);
            bufferedImage.setRGB(x / 4, y / 4, color);
        }
    }
}
