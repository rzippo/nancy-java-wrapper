package it.unipi.nancy.test;

import it.unipi.nancy.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        var curveId = new Curve(
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
        ).submit();
        var curveJson = Curve.GetJson(curveId);
        System.out.println("f: " + curveJson + "\n");

        var selfConvolutionId = Curve.Convolution(curveId, curveId);
        var selfConvolutionJson = Curve.GetJson(selfConvolutionId);
        System.out.println("f * f: " + selfConvolutionJson + "\n");

        var closureId = Curve.SubAdditiveClosure(selfConvolutionId);
        var closureJson = Curve.GetJson(closureId);
        System.out.println("subadd closure: " + closureJson + "\n");


    }
}