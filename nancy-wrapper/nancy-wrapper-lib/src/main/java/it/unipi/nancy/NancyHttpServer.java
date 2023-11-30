package it.unipi.nancy;

import java.net.URI;
import java.net.URISyntaxException;

public class NancyHttpServer
{
    public static String Host = "localhost";
    public static int Port = 1006;

    public static URI getOperationURI(String operation) throws URISyntaxException
    {
        return new URI("http://" + Host + ":" + Port + "/curve/" + operation);
    }

    public static URI getOperationURI(String id, String operation) throws URISyntaxException
    {
        return new URI("http://" + Host + ":" + Port + "/curve/" + id + "/" + operation);
    }
}
