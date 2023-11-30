package it.unipi.nancy.test;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import it.unipi.nancy.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        var curve = new Curve(
                new Sequence(
                        Arrays.asList(
                                new Point(0,0),
                                new Segment( new Rational(0), new Rational(1), new Rational(4), new Rational(3, 4)),
                                new Point( new Rational(1), new Rational(19, 4)),
                                new Segment( new Rational(1), new Rational(2), new Rational(19, 4), new Rational(3, 4))
                        )),
                new Rational(1),
                        new Rational(1),
                new Rational(3, 4)
        );

        curve.submit();
        var curveJson = Curve.retrieveJson(curve.getId());
        System.out.println("f: " + curveJson + "\n");

        var selfConvolution = Curve.convolution(curve, curve);
        var selfConvolutionJson = Curve.retrieveJson(selfConvolution.getId());
        System.out.println("f * f: " + selfConvolutionJson + "\n");

        var hdev = Curve.horizontalDeviation(curve, selfConvolution);
        System.out.println(hdev);

        var closure = Curve.subAdditiveClosure(selfConvolution);
        var closureJson = Curve.retrieveJson(closure.getId());
        System.out.println("subadd closure: " + closureJson + "\n");

    }
}