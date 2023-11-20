package it.unipi.nancy;

import java.math.BigInteger;

public class Rational {
    BigInteger _numerator;
    public BigInteger Numerator() {
        return _numerator;
    }

    BigInteger _denominator;
    public BigInteger Denominator() {
        return _denominator;
    }

    public Rational(BigInteger numerator, BigInteger denominator)
    {
        _numerator = numerator;
        _denominator = denominator;
    }

    public Rational(BigInteger numerator)
    {
        _numerator = numerator;
        _denominator = BigInteger.ONE;
    }

    public Rational(long numerator, long denominator)
    {
        _numerator = BigInteger.valueOf(numerator);
        _denominator = BigInteger.valueOf(denominator);
    }

    public Rational(long numerator)
    {
        _numerator = BigInteger.valueOf(numerator);
        _denominator = BigInteger.ONE;
    }

    public String toString(){
        return "{ num: " + Numerator() + ", den: " + Denominator() + " }";
    }
}