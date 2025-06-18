package it.unipi.nancy;

import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NancyHttpServer
{
    public static String Host = "localhost";
    public static int Port = 1006;

    public static URI getStatusURI() throws URISyntaxException
    {
        return new URI("http://" + Host + ":" + Port + "/status");
    }

    public static URI getOperationURI(String operation) throws URISyntaxException
    {
        return new URI("http://" + Host + ":" + Port + "/curve/" + operation);
    }

    public static URI getOperationURI(String id, String operation) throws URISyntaxException
    {
        return new URI("http://" + Host + ":" + Port + "/curve/" + id + "/" + operation);
    }

    public static Process runAsChild(String urls, int port) throws URISyntaxException, IOException, InterruptedException
    {
        if (testIfRunning())
            return null;

        var builder = new ProcessBuilder(
            "./nancy-http/bin/nancy-http",
            "--urls",
            "http://localhost:" + Port
        );
        var process = builder.start();

        return process;
    }

    public static boolean testIfRunning() throws URISyntaxException, InterruptedException {
        var uri = getStatusURI();
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(uri)
                .headers("Content-Type", "application/json")
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // should the status message be actually parsed?
            return response.statusCode() == 200;
        }
        catch (IOException e) {
            // assume connection issue means it is not running
            return false;
        }
    }
}
