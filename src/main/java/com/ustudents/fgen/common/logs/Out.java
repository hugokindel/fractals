package com.ustudents.fgen.common.logs;

import com.ustudents.fgen.common.Program;
import com.ustudents.fgen.common.utils.FileUtil;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Contains every functions to send output to the user.
 * For it to work properly, the terminal used for running the program should support ANSI escape sequences,
 * most UNIX terminals should support it, and modern Windows Powershell and cmd.exe should too. */
@SuppressWarnings({"unused"})
public class Out {
    /** ANSI escape sequences. */
    public static final String RESET_ANSI = "\u001B[0m";
    public static final String TEXT_BOLD = "\u001B[1m";
    public static final String TEXT_UNDERLINE = "\u001B[4m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_WHITE = "\u001B[37m";
    public static final String BACKGROUND_BLACK = "\u001B[40m";
    public static final String BACKGROUND_RED = "\u001B[41m";
    public static final String BACKGROUND_GREEN = "\u001B[42m";
    public static final String BACKGROUND_YELLOW = "\u001B[43m";
    public static final String BACKGROUND_BLUE = "\u001B[44m";
    public static final String BACKGROUND_PURPLE = "\u001B[45m";
    public static final String BACKGROUND_CYAN = "\u001B[46m";
    public static final String BACKGROUND_WHITE = "\u001B[47m";

    /** The path where log files will be stored. */
    private static final String OUTPUT_PATH = System.getProperty("java.io.tmpdir") + "/group_a/fgen";

    /** The prefix to use for every messages. */
    private static final String BASE_PREFIX = "[" + Program.getInstanceName().toLowerCase() + "]";

    /** The prefix to use for every info. */
    private static final String INFO_PREFIX = "[info]";

    /** The prefix to use for every warnings. */
    private static final String WARNING_PREFIX = "[warning]";

    /** The prefix to use for every errors. */
    private static final String ERROR_PREFIX = "[error]";

    /** The prefix to use for every debugs.. */
    private static final String DEBUG_PREFIX = "[debug]";

    /** The prefix to use for every verbose. */
    private static final String VERBOSE_PREFIX = "[verbose]";

    /** The maximum number of log files in the log folder to authorize without deletion. */
    private static final int MAX_LOG_FILES = 8;

    /** The output file to print to. */
    private static PrintWriter fileOutput;

    /** Defines if we are at the start of a line (to know if we need to prefix the printing). */
    private static boolean isStartOfLine;

    /** Defines if we should authorize the print of ANSI codes or remove them. */
    private static boolean noAnsiCode = false;

    /**
     * Starts the output system.
     *
     * Needed at the start of the program!
     * It creates the logging file, and initialize various informations.
     */
    public static void start(String[] args, boolean clear, boolean useAnsiCode) {
        boolean helpOrVersion = Arrays.stream(args)
                .anyMatch(s -> s.equals("-h") || s.equals("--help") ||s.equals("-v") || s.equals("--version"));

        // Fix to check if we are running inside IntelliJ IDEA,
        // this information is used to avoid calling any clearing process from inside the IDE terminal,
        // because if we do, it displays an unknown "" symbol.
        boolean inIntelliJ = false;
        for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if ((arg.startsWith("-javaagent") && arg.contains("JetBrains")) || arg.startsWith("-Dide=JetBrains")) {
                inIntelliJ = true;
                break;
            }
        }

        // Fix permitting to use ANSI Escape sequences on Windows Powershell and cmd (at least on updated Windows 10).
        // and also clear the terminal (on Windows and UNIX platforms).
        if (!inIntelliJ) {
            try {
                if (!helpOrVersion) {
                    if (clear) {
                        Runtime.getRuntime().exec("clear");
                    }
                } else {
                    noAnsiCode = true;
                }
            } catch (Exception ignored) {

            }
        }

        if (!useAnsiCode) {
            noAnsiCode = true;
        }

        try {
            FileUtil.createDirectoryIfNeeded(OUTPUT_PATH);
            removeOldestLogFile();
            fileOutput = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    OUTPUT_PATH +
                            "/" + Program.getInstanceName().toLowerCase() +
                            "-" + new SimpleDateFormat("yyyy_MM_dd-HH-mm-ss")
                            .format(new Date()) + ".log", false), StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            fileOutput = null;
        }

        isStartOfLine = true;
    }

    /** Shutdowns the output system.
     *
     * Needed at the end of the program!
     * It saves the logging file.
     */
    public static void end() {
        // Saves and close the output file if it exists.
        try {
            if (fileOutput != null) {
                fileOutput.flush();
                fileOutput.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void print(Object object) {
        printAndResetColor((isStartOfLine ? BASE_PREFIX + " ": "") + object);
        isStartOfLine = false;
    }

    /**
     * Prints a new line to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void println(Object object) {
        print(object + "\n");
        isStartOfLine = true;
    }

    /**
     * Prints an error to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printError(Object object) {
        printAndResetColor(TEXT_RED + (isStartOfLine ? BASE_PREFIX : "") +
                ERROR_PREFIX + RESET_ANSI + " " + object);
        isStartOfLine = false;
    }

    /**
     * Prints a new line of error to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printlnError(Object object) {
        printError(object + "\n");
        isStartOfLine = true;
    }

    /**
     * Prints a warning to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printWarning(Object object) {
        printAndResetColor(TEXT_YELLOW + (isStartOfLine ? BASE_PREFIX : "") +
                WARNING_PREFIX + RESET_ANSI + " " + object);
        isStartOfLine = false;
    }

    /**
     * Prints a new line of warning to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printlnWarning(Object object) {
        printWarning(object + "\n");
        isStartOfLine = true;
    }

    /**
     * Prints an info to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printInfo(Object object) {
        printAndResetColor(TEXT_BLUE + (isStartOfLine ? BASE_PREFIX : "") +
                INFO_PREFIX + RESET_ANSI + " " + object);
        isStartOfLine = false;
    }

    /**
     * Prints a new line of info to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printlnInfo(Object object) {
        printInfo(object + "\n");
        isStartOfLine = true;
    }

    /**
     * Prints an info to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printDebug(Object object) {
        printAndResetColor(TEXT_WHITE + (isStartOfLine ? BASE_PREFIX : "") +
                DEBUG_PREFIX + RESET_ANSI + " " + object);
        isStartOfLine = false;
    }

    /**
     * Prints a new line of info to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printlnDebug(Object object) {
        printDebug(object + "\n");
        isStartOfLine = true;
    }

    /**
     * Prints an info to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printVerbose(Object object) {
        printAndResetColor(TEXT_PURPLE + (isStartOfLine ? BASE_PREFIX : "") +
                VERBOSE_PREFIX + RESET_ANSI + " " + object);
        isStartOfLine = false;
    }

    /**
     * Prints a new line of info to output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printlnVerbose(Object object) {
        printVerbose(object + "\n");
        isStartOfLine = true;
    }


    /** Print a new line to output. */
    public static void println() {
        print("\n");
        isStartOfLine = true;
    }

    /** Clear the console. */
    public static void clear() {
        System.out.print("\033[H\033[2J");
    }

    /** Simulates a new line (used only for various scenarios in logging file). */
    static void simulateNewLine() {
        isStartOfLine = true;
        fileOutput.println();
    }

    /**
     * Prints to file output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printToFile(Object object) {
        fileOutput.print(object);
    }


    /**
     * Prints a new line to file output.
     *
     * @param object The object (with toString()) to print.
     */
    public static void printlnToFile(Object object) {
        fileOutput.println(object);
    }

    /** @return the prefix to use for every messages. */
    static String getBasePrefix() {
        return BASE_PREFIX;
    }

    /** @return the prefix to use for every warnings. */
    public static String getErrorPrefix() {
        return ERROR_PREFIX;
    }

    /** @return the prefix to use for every errors. */
    public static String getWarningPrefix() {
        return WARNING_PREFIX;
    }

    /**
     * Prints text to both system output and logging file, then reset ANSI color.
     *
     * @param object The object (with toString()) to print.
     */
    private static void printAndResetColor(Object object) {
        String text = object + RESET_ANSI;

        if (noAnsiCode) {
            System.out.print(getTextWithoutAnsiCode(text));
        } else {
            System.out.print(text);
        }

        if (fileOutput != null) {
            try {
                // We don't want to write ANSI escape sequences on the log file, as it wouldn't be recognized.
                fileOutput.print(getTextWithoutAnsiCode(text));
            } catch (Exception ignored) {

            }
        }
    }

    /** Removes the oldest log files in there is more than 8 log files. */
    private static void removeOldestLogFile() {
        File[] logFiles = new File(OUTPUT_PATH).listFiles();

        if (logFiles == null || logFiles.length < MAX_LOG_FILES) {
            return;
        }

        long oldestDate = -1;
        File oldestFile = null;

        for(File file : logFiles) {
            if(oldestDate == -1 || file.lastModified() < oldestDate) {
                oldestDate = file.lastModified();
                oldestFile = file;
            }
        }

        try {
            if (!oldestFile.delete()) {
                throw new Exception("Can't remove log file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the ANSI codes within a string using a regex expression.
     * Implementation from: https://stackoverflow.com/questions/14693701/how-can-i-remove-the-ansi-escape-sequences-from-a-string-in-python
     */
    private static String getTextWithoutAnsiCode(String text) {
        return text.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -/]*[@-~]", "");
    }

    public static void canUseAnsiCode(boolean can) {
        noAnsiCode = !can;
    }
}
