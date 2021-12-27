package com.ustudents.fgen.maths;

import com.ustudents.fgen.fractals.Fractal;
import javafx.util.Pair;

import java.util.function.Function;

public class PolynomialFunctionParser {
    public static int pos = -1;
    public static char ch;
    public static String str;

    private static void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : 0;
        while (ch == ' ') {
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
            int startPos = pos;
            if (ch == '+' || ch == '-') {
                nextChar();
            }
            while ((ch >= '0' && ch <= '9') || ch == '.') {
                nextChar();
            }
            multiplicator = Double.parseDouble(str.substring(startPos, pos));
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
                int startPos = pos;
                if (ch == '+' || ch == '-') {
                    nextChar();
                }
                while ((ch >= '0' && ch <= '9') || ch == '.') {
                    nextChar();
                }
                power = Double.parseDouble(str.substring(startPos, pos));
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

    public static Function<PolynomialFunctionValues, Complex> parse(String string) {
        str = string;
        return parse();
    }
}
