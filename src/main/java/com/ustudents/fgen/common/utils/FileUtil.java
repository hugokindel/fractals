package com.ustudents.fgen.common.utils;

import java.io.File;

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
}