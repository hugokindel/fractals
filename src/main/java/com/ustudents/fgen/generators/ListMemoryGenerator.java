package com.ustudents.fgen.generators;

public class ListMemoryGenerator extends ListGenerator {
    public int[][][] divergenceIndexesPerHandler;

    @Override
    public void generate(int width, int height) {
        divergenceIndexesPerHandler = new int[calculationHandlers.size()][height][width];

        for (int i = 0; i < calculationHandlers.size(); i++) {
            SingleMemoryGenerator generator = new SingleMemoryGenerator(calculationHandlers.get(i));
            generator.generate(width, height);
            divergenceIndexesPerHandler[i] = generator.divergenceIndexes;
        }
    }
}
