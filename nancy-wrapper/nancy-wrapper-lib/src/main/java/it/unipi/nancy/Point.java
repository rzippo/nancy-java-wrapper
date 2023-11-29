package it.unipi.nancy;

import com.fasterxml.jackson.annotation.*;

@JsonTypeName(Point.TypeCode)
@JsonPropertyOrder({
    "type",
    "time",
    "value"
})
public class Point extends Element
{
    public static final String TypeCode = "point";

    public String type = TypeCode;

    Rational _time;
    
    @JsonGetter("time")
    public Rational Time() {
        return _time;
    }

    Rational _value;
    
    @JsonGetter("value")
    public Rational Value() {
        return _value;
    }

    @JsonCreator
    @JsonIgnoreProperties()
    public Point(
            @JsonProperty("time") Rational time,
            @JsonProperty("value") Rational value
    )
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