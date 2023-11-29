package it.unipi.nancy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigInteger;

@JsonPropertyOrder({"num", "den"})
public class Rational {
    BigInteger _numerator;
    
    @JsonGetter("num")
    public BigInteger Numerator() {
        return _numerator;
    }

    BigInteger _denominator;
    
    @JsonGetter("den")
    public BigInteger Denominator() {
        return _denominator;
    }

    public Rational(
            BigInteger numerator,
            BigInteger denominator
    )
    {
        _numerator = numerator;
        _denominator = denominator;
    }

    public Rational(BigInteger numerator)
    {
        _numerator = numerator;
        _denominator = BigInteger.ONE;
    }

    @JsonCreator
    public Rational(
            @JsonProperty("num") long numerator,
            @JsonProperty("den") long denominator
    )
    {
        _numerator = BigInteger.valueOf(numerator);
        _denominator = BigInteger.valueOf(denominator);
    }

    @JsonCreator
    public Rational(long numerator)
    {
        _numerator = BigInteger.valueOf(numerator);
        _denominator = BigInteger.ONE;
    }

    public String toString(){
        return "{ \"num\": " + Numerator() + ", \"den\": " + Denominator() + " }";
    }
}