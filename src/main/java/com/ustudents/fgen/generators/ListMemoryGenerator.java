package com.ustudents.fgen.generators;

public class ListMemoryGenerator extends ListGenerator {
    public int[][][] divergenceIndexesPerHandler;

    @Override
    public void generate() {
        divergenceIndexesPerHandler = new int[calculationHandlers.size()][height][width];

        for (int i = 0; i < calculationHandlers.size(); i++) {
            SingleMemoryGenerator generator = new SingleMemoryGenerator(width, height, offsetX, offsetY, calculationHandlers.get(i));
            generator.generate();
            divergenceIndexesPerHandler[i] = generator.divergenceIndexes;
        }
    }
}
