package cz.diplomka.pivovarfe.util;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;

import java.io.IOException;
import java.util.concurrent.*;

public class ArduinoService {

    private final SerialPort serialPort;
    private ScheduledExecutorService scheduler;

    public ArduinoService() {
        serialPort = SerialPort.getCommPort("COM3"); //"/dev/ttyUSB0"
        serialPort.setComPortParameters(9600, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (serialPort.openPort()) {
            System.out.println("Pripojenie k Arduino úspešné.");
        } else {
            throw new IllegalStateException("Nepodarilo sa pripojiť k Arduino.");
        }
    }

    /**
     * Sends a command to Arduino and processes the response.
     *
     * @param command  The command to send (e.g., "GET_TEMP_MASH").
     * @param callback Callback to handle the response or error.
     */
    public void sendCommand(String command, ArduinoCallback callback) {
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            executor.submit(() -> {
                try {
                    String response = executeCommand(command);
                    Platform.runLater(() -> callback.onResponseReceived(response));
                } catch (IOException ex) {
                    Platform.runLater(() -> callback.onError(ex));
                }
            });
        }
    }

    /**
     * Starts a repeated task to send a command at fixed intervals.
     *
     * @param command         The command to send (e.g., "GET_TEMP_MASH").
     * @param callback        Callback to handle the response or error.
     * @param intervalSeconds The interval in seconds between command executions.
     */
    public void startRepeatingCommand(String command, ArduinoCallback callback, int intervalSeconds) {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                String response = executeCommand(command);
                Platform.runLater(() -> callback.onResponseReceived(response));
            } catch (IOException ex) {
                Platform.runLater(() -> callback.onError(ex));
            }
        }, 0, intervalSeconds, TimeUnit.SECONDS);
    }

    /**
     * Executes a single command and returns the response.
     *
     * @param command The command to send.
     * @return The response from the Arduino.
     * @throws IOException If there is a problem with communication.
     */
    private String executeCommand(String command) throws IOException {
        String fullCommand = command.trim() + "\n";
        serialPort.getOutputStream().write(fullCommand.getBytes());
        serialPort.getOutputStream().flush();

        byte[] buffer = new byte[1024];
        int len = serialPort.getInputStream().read(buffer);
        return new String(buffer, 0, len).trim();
    }

    /**
     * Stops the repeated command execution and closes the serial port.
     */
    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }
    }

    public interface ArduinoCallback {
        void onResponseReceived(String response);

        void onError(Exception ex);
    }
}
