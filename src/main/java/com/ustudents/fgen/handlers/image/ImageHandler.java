package com.ustudents.fgen.handlers.image;

import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.json.JsonSerializable;
import com.ustudents.fgen.common.json.JsonSerializableConstructor;
import com.ustudents.fgen.common.json.JsonSerializableType;
import com.ustudents.fgen.common.utils.ColorUtil;
import com.ustudents.fgen.format.AliasingType;
import com.ustudents.fgen.handlers.color.ColorHandler;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
        int n = aliasingType.getMultiplier();
        return new BufferedImage(divergenceIndexes[0].length / n, divergenceIndexes.length / n, BufferedImage.TYPE_INT_RGB);
    }

    protected void computeColorOfIndex(BufferedImage bufferedImage, int x, int y, int[][] divergenceIndexes, int maxIterations, AliasingType aliasingType) {
        int n = aliasingType.getMultiplier();
        y *= n;
        x *= n;
        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = y; i < y + n; i++) {
            for (int j = x; j < x + n; j++) {
                colors.add(colorHandler.computeColor(divergenceIndexes[i][j], maxIterations));
            }
        }

        bufferedImage.setRGB(x / n, y / n, ColorUtil.combineColors(colors));
    }
}
