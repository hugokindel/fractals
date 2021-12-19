package com.ustudents.fgen.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ColorUtil {
    public static int extractRed(int color) {
        return (color >> 16) & 0xFF;
    }

    public static int extractGreen(int color) {
        return (color >> 8) & 0xFF;
    }

    public static int extractBlue(int color) {
        return color & 0xFF;
    }

    public static int combineColors(List<Integer> colors) {
        List<Integer> redColors = new ArrayList<>();
        List<Integer> greenColors = new ArrayList<>();
        List<Integer> blueColors = new ArrayList<>();

        for (int color : colors) {
            redColors.add(extractRed(color));
            greenColors.add(extractGreen(color));
            blueColors.add(extractBlue(color));
        }

        int red = redColors.stream().mapToInt(Integer::intValue).sum() / redColors.size();
        int green = greenColors.stream().mapToInt(Integer::intValue).sum() / redColors.size();
        int blue = blueColors.stream().mapToInt(Integer::intValue).sum() / redColors.size();

        return (red << 16) | (green << 8) | blue;
    }
}
