package cz.diplomka.pivovarfe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.diplomka.pivovarfe.constant.ViewPath;
import cz.diplomka.pivovarfe.model.StepResponse;
import cz.diplomka.pivovarfe.service.RecipeClient;
import cz.diplomka.pivovarfe.service.SessionClient;
import cz.diplomka.pivovarfe.util.CountdownTimer;
import cz.diplomka.pivovarfe.util.SceneSwitcher;
import cz.diplomka.pivovarfe.websocket.WebSocketClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StartController {

    @FXML
    public Label actualMashTempLabel;
    @FXML
    public Label targetMashTempLabel;
    @FXML
    public Label actualWorthTempLabel;
    @FXML
    public Label targetWorthTempLabel;
    @FXML
    private Label timerLabel;

    private CountdownTimer countdownTimer;

    private Long recipeId;
    private double actualMashTemp;
    private double actualWorthTemp;

    private final RecipeClient recipeClient;
    private final SessionClient sessionClient;
    private final WebSocketClient webSocketClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public StartController() {
        this.recipeClient = new RecipeClient();
        this.sessionClient = new SessionClient();
        this.webSocketClient = new WebSocketClient(
                "ws://localhost:8080/ws",
                this::updateTemperatureLabels,
                this::handleError
        );
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
        webSocketClient.connect();
        startNextTimerAsync();
    }

    public void initialize() {
    }

    public void stopButton() {
        sessionClient.changeSessionStatus(() -> {
                    var alert = new Alert(Alert.AlertType.INFORMATION, "Varenie ukončené!");
                    alert.show();
                    webSocketClient.disconnect();
                    try {
                        SceneSwitcher.switchScene(ViewPath.MAIN_VIEW);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    var alert = new Alert(Alert.AlertType.ERROR, "Nastala chyba");
                    alert.show();
                });
    }

    private void startNextTimerAsync() {
        fetchTimerResponseFromServerAsync()
                .thenAccept(response -> Platform.runLater(() -> {
                    if (response != null) {
                        updateTargetTemperatureLabels(response.getTargetTempMash(), response.getTargetTempWorth());

                        if (response.isContinueTimer()) {
                            waitForTargetTemperature(response);
                        } else {
                            System.out.println("Timer stopped. continueTimer is false.");
                            stopTimer();
                        }
                    }
                }))
                .exceptionally(e -> {
                    Platform.runLater(() -> {
                        System.err.println("Error fetching StepResponse: " + e.getMessage());
                        stopTimer();
                    });
                    return null;
                });
    }

    private CompletableFuture<StepResponse> fetchTimerResponseFromServerAsync() {
        CompletableFuture<StepResponse> future = new CompletableFuture<>();

        recipeClient.getRecipeStep(
                this.recipeId,
                future::complete,
                () -> {
                    System.out.println("Cannot load timerResponse with recipeId: " + recipeId);
                    future.completeExceptionally(new RuntimeException("Failed to load StepResponse"));
                }
        );

        return future;
    }

    private void waitForTargetTemperature(StepResponse response) {
        try (ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1)) {

            Runnable checkTemperatureTask = () -> {
                double targetTempMash = response.getTargetTempMash();
                double targetTempWorth = response.getTargetTempWorth();

                if (actualMashTemp >= targetTempMash && actualWorthTemp >= targetTempWorth) {
                    Platform.runLater(() -> {
                        System.out.println("Target temperature reached. Starting timer...");
                        startTimer(response);
                    });

                    scheduler.shutdown();
                }
            };

            scheduler.scheduleAtFixedRate(checkTemperatureTask, 0, 1, TimeUnit.SECONDS);
        }
    }

    private void startTimer(StepResponse response) {
        countdownTimer = new CountdownTimer(
                response.getTimeInSeconds(),
                this::updateTimerLabel,
                this::startNextTimerAsync
        );
        countdownTimer.start();
    }

    private void updateTargetTemperatureLabels(double targetTempMash, double targetTempWorth) {
        targetMashTempLabel.setText(targetTempMash + "°C");
        targetWorthTempLabel.setText(targetTempWorth + "°C");
    }

    private void updateTimerLabel(int remainingSeconds) {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void stopTimer() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        timerLabel.setText("00:00");
    }

    private void updateTemperatureLabels(String message) {
        Map<String, String> temperatures;
        try {
            temperatures = objectMapper.readValue(message, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        final double mashTemperature = Double.parseDouble(temperatures.get("mashTemperature"));
        final double worthTemperature = Double.parseDouble(temperatures.get("worthTemperature"));

        actualMashTemp = mashTemperature;
        actualWorthTemp = worthTemperature;

        Platform.runLater(() -> {
            actualMashTempLabel.setText(mashTemperature + "°C");
            actualWorthTempLabel.setText(worthTemperature + "°C");
        });
    }

    private void handleError(String errorMessage) {
        Platform.runLater(() -> {
            System.out.println(errorMessage);
            actualMashTempLabel.setText("N/A");
            actualWorthTempLabel.setText("N/A");
        });
    }
}
