package com.ustudents.fgen.common.benchmark;

import java.time.Duration;
import java.time.Instant;

public class Benchmark {
    private Instant start;
    private Duration elapsedTime;

    public Benchmark() {
        start();
    }

    public void start() {
        start = Instant.now();
    }

    public Duration end() {
        elapsedTime = Duration.between(start, Instant.now());
        return elapsedTime;
    }

    public Duration getElapsedTime() {
        return elapsedTime;
    }
}
