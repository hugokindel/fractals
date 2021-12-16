package com.ustudents.fgen;

import com.ustudents.fgen.common.Program;
import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.common.options.Command;
import com.ustudents.fgen.common.options.Option;
import com.ustudents.fgen.fractals.*;
import com.ustudents.fgen.generators.*;
import com.ustudents.fgen.handlers.calculation.CalculationHandler;
import com.ustudents.fgen.handlers.calculation.PoolCalculationHandler;
import com.ustudents.fgen.handlers.image.ImageHandler;
import com.ustudents.fgen.handlers.image.SimpleImageHandler;
import com.ustudents.fgen.maths.Complex;
import com.ustudents.fgen.maths.ComplexPlane;

import java.time.Duration;

@Command(name = "fgen", version = "1.0.0", description = "A tool to generate various fractals.")
public class FGen extends Program {
    @Option(names = {"--view"}, description = "Defines which type of view to use.", usage = "<gui> or <cli>")
    protected static String viewString = "cli";

    public static Duration calculationHandlerDuration = Duration.ZERO;
    public static Duration imageHandlerDuration = Duration.ZERO;
    public static Duration gifCreation = Duration.ZERO;

    @Override
    protected int main(String[] args) {
        if (viewString.equals("gui")) {
            FGenGui.launchFgen(args);
        } else {
            /*Benchmark benchmark = new Benchmark();
            Fractal fractal = new JuliaSet(new Complex(0.285, 0.01));
            ComplexPlane plane = new ComplexPlane(new Complex(-1,1), new Complex(1,-1), 0.001);
            //CalculationHandler calculationHandler = new SingleCalculationHandler(fractal, plane, 1000, 2);
            CalculationHandler calculationHandler = new PoolCalculationHandler(fractal, plane, 1000, 2);
            Generator generator = new JpegGenerator(calculationHandler, "fractal.jpeg");
            generator.generate(4096, 4096);
            System.out.println(benchmark.end());*/

            ListImageGenerator generator = new ListImageGenerator();
            for (int i = 0; i < 10; i++) {
                Fractal fractal = new MandelbrotSet();
                ComplexPlane plane = new ComplexPlane(new Complex(-1,1), new Complex(1,-1), 0.001 + i * 0.001);
                //CalculationHandler calculationHandler = new SimpleCalculationHandler(fractal, plane, 1000, 2);
                //ImageHandler imageHandler = new SimpleImageHandler();
                CalculationHandler calculationHandler = new PoolCalculationHandler(fractal, plane, 1000, 2);
                ImageHandler imageHandler = new SimpleImageHandler();
                JpegGenerator jpegGenerator = new JpegGenerator(calculationHandler, imageHandler, "fgen-" + i + ".jpeg");
                jpegGenerator.generate(4096, 4096, 1024, 1024);
            }

            Out.println(String.format("CalculationHandler %s", calculationHandlerDuration));
            Out.println(String.format("ImageHandler %s", imageHandlerDuration));
            Out.println(String.format("GifCreation %s", gifCreation));
        }

        return 0;
    }

    public static FGen get() {
        return (FGen)Program.get();
    }
}