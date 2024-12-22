package cz.diplomka.pivovarfe.util;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ArduinoService {

    private final SerialPort serialPort;
    private ScheduledExecutorService scheduler;

    public ArduinoService(String portName, int baudRate) {
        // Inicializácia sériového portu
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setComPortParameters(baudRate, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (serialPort.openPort()) {
            System.out.println("Pripojenie k Arduino úspešné.");
        } else {
            System.out.println("Nepodarilo sa pripojiť k Arduino.");
        }
    }

    public void startReading(ArduinoCallback callback, int intervalSeconds) {
        // Spustenie plánovača na pravidelné čítanie
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> readTemperature(callback), 0, intervalSeconds, TimeUnit.SECONDS);
    }

    private void readTemperature(ArduinoCallback callback) {
        try {
            // Poslanie príkazu na získanie teploty
            serialPort.getOutputStream().write("GET_TEMP\n".getBytes());
            serialPort.getOutputStream().flush();

            // Čítanie odpovede
            byte[] buffer = new byte[1024];
            int len = serialPort.getInputStream().read(buffer);
            String response = new String(buffer, 0, len).trim();

            // Vykonanie spätnej väzby (callback)
            Platform.runLater(() -> callback.onTemperatureReceived(response));
        } catch (Exception ex) {
            Platform.runLater(() -> callback.onError(ex));
        }
    }

    public void stopReading() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown(); // Ukončenie plánovača
        }
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort(); // Zatvorenie sériového portu
        }
    }

    public interface ArduinoCallback {
        void onTemperatureReceived(String temperature);
        void onError(Exception ex);
    }
}

