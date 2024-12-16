package cz.diplomka.pivovarfe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.diplomka.pivovarfe.model.Recipe;
import javafx.concurrent.Task;
import javafx.application.Platform;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

public class RecipeClient {

    private final String baseUrl = "http://localhost:8080";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createRecipe(Recipe recipe, Runnable onSuccess, Runnable onFailure) {
        var task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                var json = objectMapper.writeValueAsString(recipe);

                var request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/recipe/create"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                        .build();

                var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    Platform.runLater(onSuccess);
                } else {
                    Platform.runLater(onFailure);
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public void getAllRecipesNames(Consumer<Map<Long, String>> onSuccess, Runnable onFailure) {
        var task = new Task<Map<Long, String>>() {
            @Override
            protected Map<Long, String> call() throws Exception {
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/recipe/names"))
                        .GET()
                        .build();

                var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return objectMapper.readValue(response.body(), new TypeReference<>() {});
                } else {
                    throw new RuntimeException("Failed to load recipes");
                }
            }
        };

        task.setOnSucceeded(event -> onSuccess.accept(task.getValue()));
        task.setOnFailed(event -> Platform.runLater(onFailure));
        new Thread(task).start();
    }

}
