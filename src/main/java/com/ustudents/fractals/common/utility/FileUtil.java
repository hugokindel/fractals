package com.ustudents.fractals.common.utility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/** Utility functions for file handling. */
public class FileUtil {
    /**
     * Creates directories recursively if needed (meaning one or more folder doesn't exist).
     *
     * @param filePath The path to use.
     */
    public static void createDirectoryIfNeeded(String filePath) throws Exception {
        File saveDirectory = new File(filePath);

        if (!saveDirectory.exists()) {
            if (!saveDirectory.mkdirs()) {
                throw new Exception("Cannot create directory at path: " + filePath + "!");
            }
        }
    }

    public static String readFileToString(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return contentBuilder.toString();

    }

    public static String getFileNameWithoutExtension(File file) {
        String fileName = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                fileName = name.replaceFirst("[.][^.]+$", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileName = "";
        }

        return fileName;

    }
}