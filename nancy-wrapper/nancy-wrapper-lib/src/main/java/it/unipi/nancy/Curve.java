package it.unipi.nancy;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Curve.class, name = Curve.TypeCode),
})
@JsonPropertyOrder({
    "type",
    "baseSequence",
    "pseudoPeriodStart",
    "pseudoPeriodLength",
    "pseudoPeriodHeight"
})
public class Curve
{
    public static final String TypeCode = "curve";

    public String type =  TypeCode;

    Sequence _baseSequence;

    @JsonGetter("baseSequence")
    public Sequence BaseSequence() throws IOException, URISyntaxException, InterruptedException
    {
        if(!isRetrieved())
            retrieve();
        return _baseSequence;
    }

    Rational _pseudoPeriodStart;

    @JsonGetter("pseudoPeriodStart")
    public Rational PseudoPeriodStart() throws IOException, URISyntaxException, InterruptedException
    {
        if(!isRetrieved())
            retrieve();
        return _pseudoPeriodStart;
    }

    Rational _pseudoPeriodLength;

    @JsonGetter("pseudoPeriodLength")
    public Rational PseudoPeriodLength() throws IOException, URISyntaxException, InterruptedException
    {
        if(!isRetrieved())
            retrieve();
        return _pseudoPeriodLength;
    }

    Rational _pseudoPeriodHeight;
    @JsonGetter("pseudoPeriodHeight")
    public Rational PseudoPeriodHeight() throws IOException, URISyntaxException, InterruptedException
    {
        if(!isRetrieved())
            retrieve();
        return _pseudoPeriodHeight;
    }

    @JsonCreator
    public Curve(
            @JsonProperty("baseSequence") Sequence baseSequence,
            @JsonProperty("pseudoPeriodStart") Rational pseudoPeriodStart,
            @JsonProperty("pseudoPeriodLength") Rational pseudoPeriodLength,
            @JsonProperty("pseudoPeriodHeight") Rational pseudoPeriodHeight
    ){
        _baseSequence = baseSequence;
        _pseudoPeriodStart = pseudoPeriodStart;
        _pseudoPeriodLength = pseudoPeriodLength;
        _pseudoPeriodHeight = pseudoPeriodHeight;
        _isRetrieved = true;
    }

    public Curve(String id) {
        _id = id;
    }

    String _id = null;

    @JsonIgnore
    public boolean isSubmitted() {
        return _id != null;
    }

    boolean _isRetrieved;

    @JsonIgnore
    public boolean isRetrieved() {
        return _isRetrieved;
    }

    @JsonIgnore
    public String getId() throws IOException, URISyntaxException, InterruptedException
    {
        if(!isSubmitted())
            submit();

        return _id;
    }

    @JsonIgnore
    public String submit() throws IOException, InterruptedException, URISyntaxException
    {
        if(!isRetrieved())
            throw new IllegalStateException("Cannot submit a curve that is not retrieved.");

        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:1006/curve"))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(getJson()))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        IfNotOkThrow(response);

        var mapper = JsonMapper.builder()
                .build();
        var rootNode = mapper.readTree(response.body());
        var id = rootNode.get("id").asText();
        _id = id;
        return id;
    }

    public Curve retrieve() throws IOException, URISyntaxException, InterruptedException
    {
        // todo: this structure is designed for support of polymorphic curves
        if(isRetrieved())
            return this;

        var json = retrieveJson(getId());
        var curve = fromJson(json);

        // fill in this curve
        _baseSequence = curve.BaseSequence();
        _pseudoPeriodStart = curve.PseudoPeriodStart();
        _pseudoPeriodLength = curve.PseudoPeriodLength();
        _pseudoPeriodHeight = curve.PseudoPeriodHeight();
        _isRetrieved = true;

        // check if the new one has a better type
        if(Curve.class != curve.getClass())
        {
            curve._id = getId();
            return curve;
        }
        else
            return this;
    }

    @JsonIgnore
    public String getJson() throws IOException, URISyntaxException, InterruptedException
    {
        if(!isRetrieved())
            retrieve();

        var json = new ObjectMapper().writeValueAsString(this);
        return json;
    }

    public static Curve fromJson(String json) throws JsonProcessingException
    {
        var mapper = JsonMapper.builder()
                .build();
        var fromJson = mapper.readValue(json, Curve.class);
        return fromJson;
    }

    public static String retrieveJson(String id) throws IOException, InterruptedException, URISyntaxException
    {
        HttpResponse<String> response;
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:1006/curve/" + id))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        IfNotOkThrow(response);
        return response.body();
    }

    public static Rational valueAt(Curve f) throws URISyntaxException, IOException, InterruptedException
    {
        return NancyHttpValueOperation(f, "valueAt", Rational.class);
    }

    public static Rational rightLimitAt(Curve f) throws URISyntaxException, IOException, InterruptedException
    {
        return NancyHttpValueOperation(f, "rightLimitAt", Rational.class);
    }

    public static Rational leftLimitAt(Curve f) throws URISyntaxException, IOException, InterruptedException
    {
        return NancyHttpValueOperation(f, "leftLimitAt", Rational.class);
    }

    public static String getCsharpCodeString(Curve f) throws URISyntaxException, IOException, InterruptedException
    {
        return NancyHttpValueOperation(f, "getCsharpCodeString", String.class);
    }

    public static Rational horizontalDeviation(Curve f, Curve g) throws URISyntaxException, IOException, InterruptedException
    {
        var operands = new ArrayList<>(List.of(f, g));
        return NancyHttpValueOperation(operands, "horizontalDeviation", Rational.class);
    }

    public static Rational verticalDeviation(Curve f, Curve g) throws URISyntaxException, IOException, InterruptedException
    {
        var operands = new ArrayList<>(List.of(f, g));
        return NancyHttpValueOperation(operands, "verticalDeviation", Rational.class);
    }

    public static Curve addition(Curve f, Curve g) throws URISyntaxException, IOException, InterruptedException
    {
        var operands = new ArrayList<>(List.of(f, g));
        return addition(operands);
    }

    public static Curve addition(List<Curve> operands) throws URISyntaxException, IOException, InterruptedException
    {
        return NancyHttpOperation(operands, "addition");
    }

    public static Curve subtraction(Curve f, Curve g) throws URISyntaxException, IOException, InterruptedException
    {
        var operands = new ArrayList<>(List.of(f, g));
        return NancyHttpOperation(operands, "subtraction");
    }

    public static Curve minimum(Curve f, Curve g) throws URISyntaxException, IOException, InterruptedException
    {
        var operands = new ArrayList<>(List.of(f, g));
        return minimum(operands);
    }

    public static Curve minimum(List<Curve> operands) throws URISyntaxException, IOException, InterruptedException
    {
        return NancyHttpOperation(operands, "minimum");
    }

    public static Curve maximum(Curve f, Curve g) throws URISyntaxException, IOException, InterruptedException
    {
        var operands = new ArrayList<>(List.of(f, g));
        return maximum(operands);
    }

    public static Curve maximum(List<Curve> operands) throws URISyntaxException, IOException, InterruptedException
    {
        return NancyHttpOperation(operands, "maximum");
    }

    public static Curve convolution(Curve f, Curve g) throws URISyntaxException, IOException, InterruptedException
    {
        var operands = new ArrayList<>(List.of(f, g));
        return convolution(operands);
    }

    public static Curve convolution(List<Curve> operands) throws URISyntaxException, IOException, InterruptedException
    {
        return NancyHttpOperation(operands, "convolution");
    }

    public static Curve maxPlusConvolution(Curve f, Curve g) throws URISyntaxException, IOException, InterruptedException
    {
        var operands = new ArrayList<>(List.of(f, g));
        return maxPlusConvolution(operands);
    }

    public static Curve maxPlusConvolution(List<Curve> operands) throws URISyntaxException, IOException, InterruptedException
    {
        return NancyHttpOperation(operands, "maxPlusConvolution");
    }

    public static Curve deconvolution(Curve f, Curve g) throws URISyntaxException, IOException, InterruptedException
    {
        var operands = new ArrayList<>(List.of(f, g));
        return NancyHttpOperation(operands, "deconvolution");
    }

    public static Curve subAdditiveClosure(Curve f) throws URISyntaxException, IOException, InterruptedException
    {
        return NancyHttpOperation(f, "subAdditiveClosure");
    }

    public static Curve NancyHttpOperation(Curve operand, String operation) throws IOException, URISyntaxException, InterruptedException
    {
        var mapper = JsonMapper.builder()
                .build();
        HttpResponse<String> response;
        var client = HttpClient.newHttpClient();
        {
            var request = HttpRequest.newBuilder()
                    .uri(NancyHttpServer.getOperationURI(operation))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            SurroundWithDoubleQuotes(operand.getId())
                    ))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        IfNotOkThrow(response);

        var rootNode = mapper.readTree(response.body());
        var resultId = rootNode.asText();
        var result = new Curve(resultId);
        return result;
    }

    public static Curve NancyHttpOperation(List<Curve> operands, String operation) throws IOException, URISyntaxException, InterruptedException
    {
        var ids = CurvesToIds(operands);
        var mapper = JsonMapper.builder()
                .build();
        HttpResponse<String> response;
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                    .uri(NancyHttpServer.getOperationURI(operation))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            mapper.writeValueAsString(ids)
                    ))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        IfNotOkThrow(response);

        var rootNode = mapper.readTree(response.body());
        var resultId = rootNode.asText();
        var result = new Curve(resultId);
        return result;
    }

    public static <T> T NancyHttpValueOperation(Curve operand, String operation, Class<T> valueType) throws IOException, URISyntaxException, InterruptedException
    {
        var mapper = JsonMapper.builder()
                .build();
        HttpResponse<String> response;
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(NancyHttpServer.getOperationURI(operand.getId(), operation))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        IfNotOkThrow(response);

        var result = mapper.readValue(response.body(), valueType);
        return result;
    }

    public static <T> T NancyHttpValueOperation(String id, String operation, Class<T> valueType) throws IOException, URISyntaxException, InterruptedException
    {
        var mapper = JsonMapper.builder()
                .build();
        HttpResponse<String> response;
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(NancyHttpServer.getOperationURI(id, operation))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        IfNotOkThrow(response);

        var result = mapper.readValue(response.body(), valueType);
        return result;
    }

    public static <T> T NancyHttpValueOperation(List<Curve> operands, String operation, Class<T> valueType) throws IOException, URISyntaxException, InterruptedException
    {
        var ids = CurvesToIds(operands);
        var mapper = JsonMapper.builder()
                .build();
        HttpResponse<String> response;
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(NancyHttpServer.getOperationURI(operation))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        mapper.writeValueAsString(ids)
                ))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        IfNotOkThrow(response);

        var result = mapper.readValue(response.body(), valueType);
        return result;
    }

    private static void IfNotOkThrow(HttpResponse<String> response) throws IOException
    {
        if(response.statusCode() != 200)
        {
            var message = response.body().isEmpty() ?
                    "HTTP response " + response.statusCode() :
                    "HTTP response " + response.statusCode() + ", " + response.body();
            throw new IOException(message);
        }
    }

    private static String SurroundWithDoubleQuotes(String text)
    {
        return "\"" + text + "\"";
    }

    private static List<String> CurvesToIds(List<Curve> curves) throws IOException, URISyntaxException, InterruptedException
    {
        var ids = new ArrayList<String>();
        for (var curve: curves)
            ids.add(curve.getId());
        return ids;
    }
}