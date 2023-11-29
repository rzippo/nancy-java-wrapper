package it.unipi.nancy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Point.class, name = Point.TypeCode),
    @JsonSubTypes.Type(value = Segment.class, name = Segment.TypeCode)
})
public abstract class Element {
    public String type;

    public abstract String toString();
}