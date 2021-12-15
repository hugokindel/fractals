package com.ustudents.fgen.generators;

public class ListMemoryGenerator extends ListGenerator {
    public int[][][] divergenceIndexesPerHandler;

    @Override
    public void generate(int width, int height) {
        divergenceIndexesPerHandler = new int[handlers.size()][height][width];

        for (int i = 0; i < handlers.size(); i++) {
            SingleMemoryGenerator generator = new SingleMemoryGenerator(handlers.get(i));
            generator.generate(width, height);
            divergenceIndexesPerHandler[i] = generator.divergenceIndexes;
        }
    }
}
