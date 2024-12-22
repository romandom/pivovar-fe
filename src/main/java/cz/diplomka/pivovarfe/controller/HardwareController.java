package cz.diplomka.pivovarfe.controller;

import cz.diplomka.pivovarfe.util.ArduinoService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class HardwareController {
    @FXML
    private Label temperatureLabel;

    @FXML
    private TextArea logArea;

    private ArduinoService arduinoService;

    @FXML
    public void initialize() {
        // Inicializácia služby Arduino
        arduinoService = new ArduinoService("/dev/ttyUSB0", 9600);
        arduinoService.startReading(new ArduinoService.ArduinoCallback() {
            @Override
            public void onTemperatureReceived(String temperature) {
                temperatureLabel.setText(temperature); // Aktualizácia Label
                logArea.appendText("Získaná teplota: " + temperature + "\n");
            }

            @Override
            public void onError(Exception ex) {
                logArea.appendText("Chyba pri čítaní teploty: " + ex.getMessage() + "\n");
            }
        }, 2); // Čítanie každé 2 sekundy
    }

    public void closeApp() {
        if (arduinoService != null) {
            arduinoService.stopReading(); // Ukončenie služby Arduino
        }
    }
}
