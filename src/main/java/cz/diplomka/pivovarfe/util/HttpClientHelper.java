package cz.diplomka.pivovarfe.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

public class HttpClientHelper {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendRequest(
            String url,
            String method,
            HttpRequest.BodyPublisher bodyPublisher,
            Consumer<HttpResponse<String>> onSuccess,
            Runnable onFailure
    ) {
        var task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json");

                HttpRequest request;
                if ("POST".equalsIgnoreCase(method)) {
                    request = requestBuilder.POST(bodyPublisher).build();
                } else if ("GET".equalsIgnoreCase(method)) {
                    request = requestBuilder.GET().build();
                } else {
                    throw new UnsupportedOperationException("HTTP method not supported: " + method);
                }

                var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    onSuccess.accept(response);
                } else {
                    Platform.runLater(onFailure);
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    public <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON", e);
        }
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON", e);
        }
    }
}

