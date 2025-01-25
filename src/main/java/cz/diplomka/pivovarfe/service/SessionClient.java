package cz.diplomka.pivovarfe.service;

import cz.diplomka.pivovarfe.util.HttpClientHelper;
import javafx.application.Platform;

import java.net.http.HttpRequest;

public class SessionClient {
    private final String baseUrl = "http://localhost:8080";
    private final HttpClientHelper httpClientHelper = new HttpClientHelper();

    public void changeSessionStatus(Runnable onSuccess, Runnable onFailure) {
        final String endpoint = baseUrl + "/session/cancelled";
        httpClientHelper.sendRequest(
                endpoint,
                "POST",
                HttpRequest.BodyPublishers.noBody(),
                response -> Platform.runLater(onSuccess),
                () -> Platform.runLater(onFailure)
        );
    }
}
