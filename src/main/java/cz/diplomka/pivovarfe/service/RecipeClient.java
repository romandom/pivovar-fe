package cz.diplomka.pivovarfe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.diplomka.pivovarfe.model.Recipe;
import cz.diplomka.pivovarfe.model.StepResponse;
import cz.diplomka.pivovarfe.util.HttpClientHelper;
import javafx.application.Platform;

import java.net.http.HttpRequest;
import java.util.Map;
import java.util.function.Consumer;

public class RecipeClient {

    private final String baseUrl = "http://localhost:8080";
    private final HttpClientHelper httpClientHelper = new HttpClientHelper();

    public void createRecipe(Recipe recipe, Runnable onSuccess, Runnable onFailure) {
        final String endpoint = baseUrl + "/recipe/create";
        httpClientHelper.sendRequest(
                endpoint,
                "POST",
                HttpRequest.BodyPublishers.ofString(httpClientHelper.toJson(recipe)),
                response -> Platform.runLater(onSuccess),
                () -> Platform.runLater(onFailure)
        );
    }

    public void deleteRecipe(Long id, Runnable onSuccess, Runnable onFailure) {
        final String endpoint = baseUrl + "/recipe/delete/" + id;
        httpClientHelper.sendRequest(
                endpoint,
                "DELETE",
                HttpRequest.BodyPublishers.noBody(),
                response -> Platform.runLater(onSuccess),
                () -> Platform.runLater(onFailure)
        );
    }

    public void getAllRecipesNames(Consumer<Map<Long, String>> onSuccess, Runnable onFailure) {
        final String endpoint = baseUrl + "/recipe/names";
        httpClientHelper.sendRequest(
                endpoint,
                "GET",
                HttpRequest.BodyPublishers.noBody(),
                response -> {
                    Map<Long, String> result = httpClientHelper.fromJson(response.body(), new TypeReference<>() {
                    });
                    Platform.runLater(() -> onSuccess.accept(result));
                },
                () -> Platform.runLater(onFailure)
        );
    }

    public void getRecipeDetail(Long id, Consumer<Recipe> onSuccess, Runnable onFailure) {
        final String endpoint = baseUrl + "/recipe/" + id;
        httpClientHelper.sendRequest(
                endpoint,
                "GET",
                HttpRequest.BodyPublishers.noBody(),
                response -> {
                    Recipe recipe = httpClientHelper.fromJson(response.body(), Recipe.class);
                    Platform.runLater(() -> onSuccess.accept(recipe));
                },
                () -> Platform.runLater(onFailure)
        );
    }

    public void getRecipeStep(Long id, Consumer<StepResponse> onSuccess, Runnable onFailure) {
        String endpoint = baseUrl + "/recipe/" + id + "/step";
        httpClientHelper.sendRequest(
                endpoint,
                "GET",
                HttpRequest.BodyPublishers.noBody(),
                response -> {
                    StepResponse stepResponse = httpClientHelper.fromJson(response.body(), StepResponse.class);
                    Platform.runLater(() -> onSuccess.accept(stepResponse));
                },
                () -> Platform.runLater(onFailure)
        );
    }
}
