package com.ustudents.fractals.common.utility;

import com.ustudents.fractals.common.BaseConfiguration;
import com.ustudents.fractals.common.json.Json;

import java.io.File;

/** Contains useful function to find various paths for the project's data. */
@SuppressWarnings({"unused"})
public class Resources {
    private static final String dataDirectoryName = "data";
    private static final String logsDirectoryName = "logs";
    private static final String settingsFilename = "settings.json";
    private static BaseConfiguration config = null;

    /**
     * Gets the data directory's path.
     *
     * @return the path.
     */
    public static String getDataDirectory() {
        return createPathIfNeeded(dataDirectoryName);
    }

    /**
     * Gets the logs directory's path.
     *
     * @return the path.
     */
    public static String getLogsDirectory() {
        return createPathIfNeeded(getDataDirectory() + "/" + logsDirectoryName);
    }

    /**
     * Creates a path if needed.
     *
     * @param filepath The path to use.
     * @return the path.
     */
    private static String createPathIfNeeded(String filepath) {
        try {
            FileUtil.createDirectoryIfNeeded(filepath);
            return filepath;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /** Loads the settings into memory. */
    public static void loadConfig() {
        if (config == null) {
            try {
                File file = new File(getDataDirectory() + "/" + settingsFilename);

                if (file.exists()) {
                    config = Json.deserialize(getDataDirectory() + "/" + settingsFilename, BaseConfiguration.class);
                } else {
                    config = new BaseConfiguration();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Saves the settings on the hard drive. */
    public static void saveConfig(String message) {
        try {
            if (config != null) {
                Json.serialize(getDataDirectory() + "/" + settingsFilename, config, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BaseConfiguration getConfig() {
        return config;
    }
}
