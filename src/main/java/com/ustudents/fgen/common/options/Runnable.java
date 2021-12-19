package com.ustudents.fgen.common.options;

import com.ustudents.fgen.common.logs.Out;
import com.ustudents.fgen.common.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * This class defines a runnable command, which means it can read arguments back from the CLI and set any @Option
 * attributes. Some syntax is inspired from: https://picocli.info */
public abstract class Runnable {
    /** Option to show the help message. */
    @Option(names = {"-h", "--help"}, description = "Show this help message.")
    protected boolean showHelp = false;

    /** Option to show the version message. */
    @Option(names = {"-v", "--version"}, description = "Show the version number.")
    protected boolean showVersion = false;

    /**
     * Read every arguments provided and try to see if any option is corresponding to define their values.
     *
     * @param args The arguments.
     * @param classWithArgs The child's class.
     * @param <T> The type of the child class.
     */
    protected <T extends Runnable> boolean parse(String[] args, Class<T> classWithArgs) {
        if (args.length == 0) {
            showHelp = true;
        }

        List<String> formattedArgs = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.startsWith("-")) {
                formattedArgs.add(arg);
                while (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    i++;
                    boolean hasEqual = formattedArgs.get(formattedArgs.size() - 1).contains("=");
                    String updated = formattedArgs.get(formattedArgs.size() - 1) + (hasEqual ? " " : "=") + args[i];
                    formattedArgs.set(formattedArgs.size() - 1, updated);
                }
            }
        }

        Set<Field> fields = new LinkedHashSet<>(Arrays.asList(Runnable.class.getDeclaredFields()));
        Class<?> currentType = classWithArgs;
        while (currentType != null) {
            fields.addAll(Arrays.asList(currentType.getDeclaredFields()));
            currentType = currentType.getSuperclass();
        }
        fields.removeIf(field -> !field.isAnnotationPresent(Option.class));

        for (String arg : formattedArgs) {
            if (arg.startsWith("-")) {
                String[] parts = arg.split("=");
                boolean found = false;

                for (Field field : fields) {
                    for (String name : field.getAnnotation(Option.class).names()) {
                        if (parts[0].equals(name)) {
                            found = true;

                            try {
                                field.setAccessible(true);
                                if (field.getType() == boolean.class) {
                                    field.set(this, true);
                                } else if (parts.length == 1) {
                                    Out.printlnError("Option '" + name + "' called with no values when one was expected (please use the -h command).");
                                } else {
                                    field.set(this, parse(parts[1], field.getType()));
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                    }

                    if (found) {
                        break;
                    }
                }

                if (!found) {
                    displayUnknownOption(parts[0], fields);
                }
            }
        }

        assert classWithArgs != null;

        if (showHelp) {
            displayHelp(classWithArgs, fields);
        }

        if (showVersion) {
            displayVersion(classWithArgs);
        }

        return true;
    }

    /**
     * Parse the value of an option.
     *
     * @param value The value (what comes after the '=' character in an option).
     * @param classType The class type of the option.
     * @return the value converted to the class type of the option.
     */
    public static Object parse(String value, Class<?> classType) {
        if (classType == String[].class) {
            return value.split(",");
        } else if (classType == String.class) {
            if (value.startsWith("\"") && value.endsWith("\"")) {
                return value.substring(1, value.length() - 1);
            }
            return value;
        } else if (classType == char.class) {
            return value.charAt(0);
        } else if (classType == boolean.class) {
            return true;
        } else if (classType == int.class) {
            return Integer.parseInt(value);
        } else if (classType == byte.class) {
            return Byte.parseByte(value);
        } else if (classType == short.class) {
            return Short.parseShort(value);
        } else if (classType == long.class) {
            return Long.parseLong(value);
        } else if (classType == float.class) {
            return Float.parseFloat(value);
        } else if (classType == double.class) {
            return Double.parseDouble(value);
        }

        Out.printlnError("Cannot parse an unknown type.");
        return null;
    }

    /**
     * Shows the unknown option along with the closest one (if found).
     *
     * @param unknownOption The unknown option.
     * @param fields The fields to search in.
     */
    private void displayUnknownOption(String unknownOption, Set<Field> fields) {
        int distance = -1;
        String nearest = "";

        for (Field field : fields) {
            Option option = field.getAnnotation(Option.class);

            for (String name : option.names()) {
                int optionDistance = StringUtil.computeLevenshteinDistance(unknownOption, name);

                if (distance == -1 || optionDistance < distance) {
                    distance = optionDistance;
                    nearest = name;
                }
            }
        }

        Out.println("Unknown option '" + unknownOption + "'!");

        if (!nearest.isEmpty()) {
            Out.println("Did you mean '" + nearest + "'?");
        }
    }

    /**
     * Shows the program's version.
     *
     * @param classWithArgs The child's class.
     * @param <T> The type of the child's class.
     */
    private <T extends Runnable> void displayVersion(Class<T> classWithArgs) {
        Out.println("Version: " + classWithArgs.getAnnotation(Command.class).version());
    }

    /**
     * Shows the program's help message.
     *
     * @param classWithArgs The child's class.
     * @param fields The list of fields to show.
     * @param <T> The type of the child's class.
     */
    private <T extends Runnable> void displayHelp(Class<T> classWithArgs, Set<Field> fieldsSet) {
        List<Field> fields = new ArrayList<>(fieldsSet.stream().toList());
        fields.add(fields.remove(1));
        fields.add(fields.remove(0));

        Out.println("usage: ./" + classWithArgs.getAnnotation(Command.class).name() + ".jar [options...]");

        Out.println();

        for (String line : classWithArgs.getAnnotation(Command.class).description()) {
            Out.println(line);
        }

        Out.println();

        Out.println("Options:");
        for (Field field : fields) {
            Option option = field.getAnnotation(Option.class);
            int numberOfNames = option.names().length;

            Out.print(" \t");

            for (int i = 0; i < numberOfNames; i++) {
                Out.print(option.names()[i] + (i == numberOfNames - 1 ? "" : ", "));
            }

            if (!option.usage().isEmpty()) {
                Out.print("=" + option.usage());
            }

            Out.println();

            for (String line : option.description()) {
                Out.print(" \t\t");
                Out.println(line);
            }
        }
    }

    /**
     * Run this command.
     *
     * @param args The arguments.
     * @return the return code.
     */
    public abstract int run(String[] args);
}
