package it.unipi.nancy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Curve {
    Sequence _sequence;
    public Sequence Sequence() {
        return _sequence;
    }

    Rational _pseudoPeriodStart;
    public Rational PseudoPeriodStart() {
        return _pseudoPeriodStart;
    }

    Rational _pseudoPeriodLength;
    public Rational PseudoPeriodLength() {
        return _pseudoPeriodLength;
    }

    Rational _pseudoPeriodHeight;
    public Rational PseudoPeriodHeight() {
        return _pseudoPeriodHeight;
    }

    public Curve(Sequence sequence, Rational pseudoPeriodStart, Rational pseudoPeriodLength, Rational pseudoPeriodHeight){
        _sequence = sequence;
        _pseudoPeriodStart = pseudoPeriodStart;
        _pseudoPeriodLength = pseudoPeriodLength;
        _pseudoPeriodHeight = pseudoPeriodHeight;
    }

    Integer _id = null;

    public boolean isSubmitted() {
        return _id != null;
    }

    public int getId() throws IOException, URISyntaxException, InterruptedException
    {
        if(isSubmitted())
            return _id;
        else
        {
            submit();
            return _id;
        }
    }

    public int submit() throws IOException, InterruptedException, URISyntaxException
    {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:1006/curve"))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(toString()))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200)
            throw new IOException("HTTP response " + response.statusCode());
        var id = Integer.parseInt(response.body());
        _id = id;
        return id;
    }

    public String toString() {
        return "{ " +
                "\"type\": \"curve\", " +
                "\"sequence\": " + Sequence().toString() + ", " +
                "\"periodStart\": " + PseudoPeriodStart().toString() + ", " +
                "\"periodLength\": " + PseudoPeriodLength().toString() + ", " +
                "\"periodHeight\": " + PseudoPeriodHeight().toString() +
                " }";
    }

    public static String GetJson(int id) throws IOException, InterruptedException, URISyntaxException
    {
        HttpResponse<String> response;
        try (var client = HttpClient.newHttpClient())
        {
            var request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:1006/curve/" + id))
                    .headers("Content-Type", "application/json")
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        if(response.statusCode() != 200)
            throw new IOException("HTTP response " + response.statusCode());
        return response.body();
    }

    public static int Convolution(int f, int g) throws URISyntaxException, IOException, InterruptedException
    {
        HttpResponse<String> response;
        try (var client = HttpClient.newHttpClient())
        {
            var request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:1006/curve/convolution"))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{ \"f\": " + f + ", \"g\": " + g + " }"
                    ))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        if(response.statusCode() != 200)
            throw new IOException("HTTP response " + response.statusCode());
        var id = Integer.parseInt(response.body());
        return id;
    }

    public static int Deconvolution(int f, int g) throws URISyntaxException, IOException, InterruptedException
    {
        HttpResponse<String> response;
        try (var client = HttpClient.newHttpClient())
        {
            var request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:1006/curve/deconvolution"))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{ \"f\": " + f + ", \"g\": " + g + " }"
                    ))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        if(response.statusCode() != 200)
            throw new IOException("HTTP response " + response.statusCode());
        var id = Integer.parseInt(response.body());
        return id;
    }

    public static int SubAdditiveClosure(int f) throws URISyntaxException, IOException, InterruptedException
    {
        HttpResponse<String> response;
        try (var client = HttpClient.newHttpClient())
        {
            var request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:1006/curve/subadditive-closure"))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(f)))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        if(response.statusCode() != 200)
            throw new IOException("HTTP response " + response.statusCode());
        var id = Integer.parseInt(response.body());
        return id;
    }


}