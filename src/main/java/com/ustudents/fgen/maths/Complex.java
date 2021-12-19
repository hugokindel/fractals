package com.ustudents.fgen.maths;

import com.ustudents.fgen.common.json.JsonSerializable;

@JsonSerializable
public class Complex implements Cloneable {
    @JsonSerializable
    public Double real = 0.;

    @JsonSerializable
    public Double imaginary = 0.;

    public Complex() {

    }

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getModulus() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    public Complex add(Complex addend) {
        return new Complex(
                real + addend.real,
                imaginary + addend.imaginary);
    }

    public Complex subtract(Complex subtrahend) {
        return new Complex(
                real - subtrahend.real,
                imaginary - subtrahend.imaginary);
    }

    public Complex multiply(Complex factor) {
        return new Complex(
                real * factor.real - imaginary * factor.imaginary,
                real * factor.imaginary + imaginary * factor.real);
    }

    public Complex multiply(double factor) {
        return new Complex(
                real * factor - imaginary * factor,
                real * factor + imaginary * factor);
    }

    /* From Apache. */
    public Complex log() {
        return new Complex(Math.log(getModulus()), Math.atan2(imaginary, real));
    }

    /* From Apache. */
    public Complex exp() {
        double expOfReal = Math.exp(real);
        return new Complex(expOfReal * Math.cos(imaginary), expOfReal * Math.sin(imaginary));
    }

    /* From Apache. */
    public Complex pow(Complex factor) {
        return log().multiply(factor).exp();
    }

    /* From Apache. */
    public Complex pow(double factor) {
        return log().multiply(factor).exp();
    }

    public Complex divide(Complex divisor) {
        double denominator = divisor.real * divisor.real + divisor.imaginary * divisor.imaginary;
        return new Complex(
                (real * divisor.real + imaginary * divisor.imaginary) / denominator,
                (imaginary * divisor.real - real * divisor.imaginary) / denominator);
    }

    public static Complex fromPolarCoordinates(double rho, double theta) {
        return new Complex(rho * Math.cos(theta),rho * Math.sin(theta));
    }

    @Override
    public Complex clone() {
        final Complex clone;

        try {
            clone = (Complex)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        clone.real = real;
        clone.imaginary = imaginary;

        return clone;
    }

    @Override
    public String toString() {
        return "Complex{real=" + real + ", imaginary=" + imaginary + "}";
    }
}