package com.ustudents.fgen.generators;

import com.ustudents.fgen.handlers.CalculationHandler;

import java.util.ArrayList;
import java.util.List;

public class ListMemoryGenerator extends Generator {
    public List<CalculationHandler> handlers = new ArrayList<>();
    public int[][][] divergenceIndexesPerHandler;

    public void addCalculationHandler(CalculationHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void generate(int width, int height) {
        divergenceIndexesPerHandler = new int[handlers.size()][height][width];

        for (int i = 0; i < handlers.size(); i++) {
            divergenceIndexesPerHandler[i] = handlers.get(i).calculateDivergenceIndexes(width, height);
        }
    }
}
