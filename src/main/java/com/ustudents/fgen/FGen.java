package com.ustudents.fgen;

import com.ustudents.fgen.common.Program;
import com.ustudents.fgen.common.benchmark.Benchmark;
import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.common.options.Command;
import com.ustudents.fgen.common.options.Option;
import com.ustudents.fgen.fractals.*;
import com.ustudents.fgen.generators.*;
import com.ustudents.fgen.handlers.CalculationHandler;
import com.ustudents.fgen.handlers.PoolCalculationHandler;
import com.ustudents.fgen.handlers.SingleCalculationHandler;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

@Command(name = "fgen", version = "1.0.0", description = "A tool to generate various fractals.")
public class FGen extends Program {
    @Option(names = {"--view"}, description = "Defines which type of view to use.", usage = "<gui> or <cli>")
    protected static String viewString = "cli";

    @Override
    protected int main(String[] args) {
        if (viewString.equals("gui")) {
            FGenGui.launchFgen(args);
        } else {
            /*Benchmark benchmark = new Benchmark();
            Fractal fractal = new JuliaSet(new Complex(0.285, 0.01));
            ComplexPlane plane = new ComplexPlane(new Complex(-1,1), new Complex(1,-1), 0.001);
            //CalculationHandler handler = new SingleCalculationHandler(fractal, plane, 1000, 2);
            CalculationHandler handler = new PoolCalculationHandler(fractal, plane, 1000, 2);
            Generator generator = new JpegGenerator(handler, "fractal.jpeg");
            generator.generate(4096, 4096);
            System.out.println(benchmark.end());*/

            Benchmark benchmark = new Benchmark();
            ListGenerator generator = new ListMemoryGenerator();
            for (int i = 0; i < 10; i++) {
                Fractal fractal = new JuliaSet(new Complex(0.285 + i * 0.01, 0.01));
                ComplexPlane plane = new ComplexPlane(new Complex(-1,1), new Complex(1,-1), 0.001);
                CalculationHandler handler = new SingleCalculationHandler(fractal, plane, 1000, 2);
                //CalculationHandler handler = new PoolCalculationHandler(fractal, plane, 1000, 2);
                generator.addCalculationHandler(handler);
            }
            generator.generate(4096, 4096);
            System.out.println(benchmark.end());
        }

        return 0;
    }

    public static FGen get() {
        return (FGen)Program.get();
    }
}