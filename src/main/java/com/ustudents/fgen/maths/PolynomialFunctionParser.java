package com.ustudents.fgen.maths;

import java.util.function.Function;

/**
 * Uses a recursive descent parser to parse a polynomial function from a string.
 * A polynomial function can be for example of the given form `5z^7 + -2z^3 - 5z + c`.
 */
public class PolynomialFunctionParser {
    public static int pos = -1;
    public static char ch;
    public static String str;

    private static void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : 0;
        if (ch == ' ') {
            nextChar();
        }
    }

    private static Function<PolynomialFunctionValues, Complex> parse() {
        nextChar();
        Function<PolynomialFunctionValues, Complex> function = parseElement();

        while (true) {
            if (ch == '+' || ch == '-') {
                int type = ch;
                nextChar();

                Function<PolynomialFunctionValues, Complex> function2 = parseElement();

                assert function2 != null;
                assert function != null;

                Function<PolynomialFunctionValues, Complex> functionCopy = function;
                if (type == '+') {
                    function = v -> functionCopy.apply(v).add(function2.apply(v));
                } else {
                    function = v -> functionCopy.apply(v).subtract(function2.apply(v));
                }
            } else if (ch == 0) {
                return function;
            } else {
                return null;
            }
        }
    }

    private static Function<PolynomialFunctionValues, Complex> parseElement() {
        char varName;
        double multiplicator;
        double power;

        if ((ch >= '0' && ch <= '9') || ch == '.' || ch == '+' || ch == '-') {
            multiplicator = parseNumber();
        } else if (ch == 'z' || ch == 'c') {
            multiplicator = 1;
        } else {
            return null;
        }

        if (ch == 'z' || ch == 'c') {
            varName = ch;
            nextChar();
        } else {
            return null;
        }

        if (ch == '^') {
            nextChar();

            if ((ch >= '0' && ch <= '9') || ch == '.' || ch == '+' || ch == '-') {
                power = parseNumber();
            } else {
                return null;
            }
        } else if (ch == 0 || ch == '+' || ch == '-') {
            power = 1;
        } else {
            return null;
        }

        if (varName == 'z') {
            return v -> new Complex(v.z.real, v.z.imaginary).pow(power).multiply(multiplicator);
        }

        return v -> new Complex(v.c.real, v.c.imaginary).pow(power).multiply(multiplicator);
    }

    private static double parseNumber() {
        int startPos = pos;

        if (ch == '+' || ch == '-') {
            nextChar();
        }

        while ((ch >= '0' && ch <= '9') || ch == '.') {
            nextChar();
        }

        return Double.parseDouble(str.substring(startPos, pos));
    }

    public static Function<PolynomialFunctionValues, Complex> parse(String string) {
        str = string;
        pos = -1;
        return parse();
    }
}
