package it.unipi.nancy;

public class Segment extends Element {
    Rational _startTime;
    public Rational StartTime() {
        return _startTime;
    }

    Rational _endTime;
    public Rational EndTime() {
        return _endTime;
    }

    Rational _rightLimitOnStartTime;
    public Rational RightLimitOnStartTime() {
        return _rightLimitOnStartTime;
    }

    Rational _slope;
    public Rational Slope() {
        return _slope;
    }

    public Segment(Rational startTime, Rational endTime, Rational rightLimitOnStartTime, Rational slope)
    {
        _startTime = startTime;
        _endTime = endTime;
        _rightLimitOnStartTime = rightLimitOnStartTime;
        _slope = slope;
    }

    public String toString(){
        return "{ " +
                "\"type\": \"segment\", " +
                "\"startTime\": " + StartTime() + ", " +
                "\"endTime\": " + EndTime() + ", " +
                "\"rightLimitOnStartTime\": " + RightLimitOnStartTime() + ", " +
                "\"slope\": " + Slope() +
                " }";
    }
}