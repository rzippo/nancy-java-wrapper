package it.unipi.nancy;

public class Point extends Element {
    Rational _time;
    public Rational Time() {
        return _time;
    }

    Rational _value;
    public Rational Value() {
        return _value;
    }

    public Point(Rational time, Rational value)
    {
        _time = time;
        _value = value;
    }

    public Point(int time, int value)
    {
        _time = new Rational(time);
        _value = new Rational(value);
    }

    public String toString(){
        return "{ " +
                "\"type\": \"point\", " +
                "\"time\": " + Time() + ", " +
                "\"value\": " + Value() +
                " }";
    }
}