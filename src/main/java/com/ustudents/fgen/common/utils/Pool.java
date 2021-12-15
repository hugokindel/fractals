package com.ustudents.fgen.common.utils;

import java.util.concurrent.ForkJoinPool;

public class Pool {
    private static ForkJoinPool pool = null;

    public static ForkJoinPool get(int parallelismLevel) {
        if (pool != null && pool.getParallelism() != parallelismLevel) {
            pool.shutdown();
            pool = null;
        }

        if (pool == null) {
            pool = new ForkJoinPool(parallelismLevel);
        }

        return pool;
    }
}
