package it.unipi.nancy;

import com.fasterxml.jackson.annotation.*;

@JsonTypeName(Segment.TypeCode)
@JsonPropertyOrder({
    "type",
    "startTime",
    "endTime",
    "rightLimitAtStartTime",
    "slope"
})
public class Segment extends Element
{
    public static final String TypeCode = "segment";

    public String type = TypeCode;

    Rational _startTime;
    @JsonGetter("startTime")
    public Rational StartTime() {
        return _startTime;
    }

    Rational _endTime;
    @JsonGetter("endTime")
    public Rational EndTime() {
        return _endTime;
    }

    Rational _rightLimitAtStartTime;
    @JsonGetter("rightLimitAtStartTime")
    public Rational rightLimitAtStartTime() {
        return _rightLimitAtStartTime;
    }

    Rational _slope;
    @JsonGetter("slope")
    public Rational Slope() {
        return _slope;
    }

    @JsonCreator
    public Segment(
            @JsonProperty("startTime") Rational startTime,
            @JsonProperty("endTime") Rational endTime,
            @JsonProperty("rightLimitAtStartTime") Rational rightLimitAtStartTime,
            @JsonProperty("slope") Rational slope
    )
    {
        _startTime = startTime;
        _endTime = endTime;
        _rightLimitAtStartTime = rightLimitAtStartTime;
        _slope = slope;
    }

    public String toString(){
        return "{ " +
                "\"type\": \"segment\", " +
                "\"startTime\": " + StartTime() + ", " +
                "\"endTime\": " + EndTime() + ", " +
                "\"rightLimitAtStartTime\": " + rightLimitAtStartTime() + ", " +
                "\"slope\": " + Slope() +
                " }";
    }
}