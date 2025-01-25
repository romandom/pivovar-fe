package cz.diplomka.pivovarfe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.diplomka.pivovarfe.websocket.WebSocketClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Map;


public class HardwareController {
    @FXML
    private Label mashTemperatureLabel;

    @FXML
    private Label worthTemperatureLabel;

    private final WebSocketClient webSocketClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public HardwareController() {
        this.webSocketClient = new WebSocketClient(
                "ws://localhost:8080/ws",
                this::updateTemperatureLabels,
                this::handleError
        );
    }

    @FXML
    public void initialize() {
        webSocketClient.connect();
    }

    private void updateTemperatureLabels(String message) {

        Map<String, String> temperatures;
        try {
            temperatures = objectMapper.readValue(message, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        final String mashTemperature = temperatures.get("mashTemperature");
        final String worthTemperature = temperatures.get("worthTemperature");

        mashTemperatureLabel.setText(mashTemperature + "°C");
        worthTemperatureLabel.setText(worthTemperature + "°C");

    }

    private void handleError(String errorMessage) {
        System.out.println(errorMessage);
        mashTemperatureLabel.setText("N/A" + "°C");
        worthTemperatureLabel.setText("N/A" + "°C");
    }
}
