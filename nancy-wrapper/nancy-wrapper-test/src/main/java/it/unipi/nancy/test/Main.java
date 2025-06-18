package it.unipi.nancy.test;

import it.unipi.nancy.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

public class Main {
    public static Process ChildProcess;

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        if(!NancyHttpServer.testIfRunning()) {
            System.out.println("NancyHttpServer is not running. Running it as child.");

            NancyHttpServer.Host = "localhost";
            ChildProcess = NancyHttpServer.runAsChild("localhost", NancyHttpServer.Port);
            if(ChildProcess == null) {
                System.out.println("NancyHttpServer could not start as the child process.");
                return;
            }
            else if(!ChildProcess.isAlive()) {
                var stdout_stream = ChildProcess.getInputStream();
                var stdout_text = IOUtils.toString(stdout_stream, StandardCharsets.UTF_8);
                System.out.println("NancyHttpServer could not start as the child process:");
                System.out.println(stdout_text);
                return;
            }
        }

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

        var value = Curve.rightLimitAt(closure, new Rational(10));
        System.out.println(value);

        if(ChildProcess != null) {
            if(SystemUtils.IS_OS_WINDOWS) {
                ProcessBuilder pb = new ProcessBuilder(
                        "taskkill",
                        "/PID", Long.toString(ChildProcess.pid()),
                        "/F"
                );
                pb.start();
            }
            else {
                // assume linux
                ProcessBuilder pb = new ProcessBuilder(
                        "kill",
                        "-9",
                        Long.toString(ChildProcess.pid())
                );
                pb.start();
            }

            System.out.println("Sent the interrupt to the child server process");
            var success = ChildProcess.waitFor(2, TimeUnit.SECONDS);
            if(success) {
                System.out.println("Child process terminated.");
                var stdout_stream = ChildProcess.getInputStream();
                var stdout_text = IOUtils.toString(stdout_stream, StandardCharsets.UTF_8);
                System.out.println("NancyHttpServer could not start as the child process:");
                System.out.println(stdout_text);
            }
            else {
                System.out.println("Child process NOT terminated.");
            }
        }
    }
}