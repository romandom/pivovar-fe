package cz.diplomka.pivovarfe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.diplomka.pivovarfe.model.Recipe;
import javafx.concurrent.Task;
import javafx.application.Platform;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class RecipeClient {

    private final String baseUrl = "http://localhost:8080";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createRecipe(Recipe recipe, Runnable onSuccess, Runnable onFailure) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String json = objectMapper.writeValueAsString(recipe);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/recipe/create"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 201) {
                    Platform.runLater(onSuccess);
                } else {
                    Platform.runLater(onFailure);
                }
                return null;
            }
        };

        new Thread(task).start();
    }
}
