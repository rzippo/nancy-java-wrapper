package it.unipi.nancy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    List<Element> _elements;

    @JsonGetter("elements")
    public List<Element> Elements() {
        return _elements;
    }

    @JsonCreator
    public Sequence(
            @JsonProperty("elements") List<Element> elements
    ) {
        _elements = elements;
    }

    public String toString(){
        var s = "{ ";
        s += "\"elements\": [";
        for (int i = 0; i < _elements.size(); i++) {
            var element = _elements.get(i);
            s += element;
            if(i < _elements.size() - 1)
                s += ", ";
        }
        s += " ] }";

        return s;
    }
}