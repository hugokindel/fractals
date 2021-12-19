package com.ustudents.fgen;

import com.ustudents.fgen.common.Program;
import com.ustudents.fgen.common.json.Json;
import com.ustudents.fgen.common.json.JsonReader;
import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.common.options.Command;
import com.ustudents.fgen.common.options.Option;
import com.ustudents.fgen.generators.Generator;

import java.io.InputStream;
import java.time.Duration;

@Command(name = "fgen", version = "1.0.0", description =
        "A tool to generate fractals based on various sets of parameters.\n" +
        "Fractals can be generated by providing a configuration file or through a GUI.")
public class FGen extends Program {
    @Option(names = {"-t", "--type"}, description = "Defines which type of view to use.", usage = "\"gui\" or \"cli\"")
    protected static String type = "cli";

    @Option(names = {"-f", "--load-file"}, description = "Loads and run a configuration file stored at <filepath>.", usage = "<filepath>")
    protected static String loadFilepath = null;

    @Option(names = {"-p", "--load-preset"}, description = "Loads and run a configuration preset file.", usage = "\"default\"")
    protected static String presetName = null;

    @Option(names = {"-s", "--save"}, description = "Defines a filepath to save the loaded configuration.", usage = "<filepath>")
    protected static String saveFilepath = null;

    @Option(names = {"-l", "--load-only"}, description =
            "Defines that the loaded configuration should not be run.\n" +
            "Useful if you want to only load and save a configuration.")
    protected static boolean shouldOnlyLoad = false;

    public Configuration loadedConfiguration = null;

    public static Duration calculationHandlerDuration = Duration.ZERO;
    public static Duration imageHandlerDuration = Duration.ZERO;
    public static Duration gifCreation = Duration.ZERO;

    @Override
    protected int main(String[] args) {
        if (loadFilepath != null) {
            loadedConfiguration = Json.deserialize(loadFilepath, Configuration.class);
        } else if (presetName != null) {
            loadedConfiguration = Json.deserializeFromResources("/presets/" + presetName + ".json", Configuration.class);

            if (loadedConfiguration == null) {
                Out.printError("Trying to load an unknown preset!");
            }
        }

        if (type.equals("gui")) {
            FGenGui.launchFgen(args);
        } else {
            if (loadedConfiguration != null) {
                if (!shouldOnlyLoad) {
                    for (Generator generator : loadedConfiguration.generators) {
                        generator.generate();
                    }
                }

                if (saveFilepath != null) {
                    Json.serialize(saveFilepath, loadedConfiguration);
                }
            }
        }

        return 0;
    }

    public static FGen get() {
        return (FGen)Program.get();
    }
}