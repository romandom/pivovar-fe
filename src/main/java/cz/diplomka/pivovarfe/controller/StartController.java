package cz.diplomka.pivovarfe.controller;

import cz.diplomka.pivovarfe.model.TimerResponse;
import cz.diplomka.pivovarfe.util.CountdownTimer;
import javafx.fxml.FXML;

import javafx.scene.control.Label;


public class StartController {

    @FXML
    private Label timerLabel;

    private CountdownTimer countdownTimer;

    public void setRecipeId(Long recipeId) {

        //loadRecipeDetails(recipeId);
    }

    public void initialize() {
        startNextTimer();
    }

    private void startNextTimer() {
        TimerResponse response = fetchTimerResponseFromServer();

        if (response.isContinueTimer()) {
            countdownTimer = new CountdownTimer(
                    response.getTimeInSeconds(),
                    this::updateTimerLabel,
                    this::startNextTimer
            );
            countdownTimer.start();
        } else {
            stopTimer();
        }
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
        timerLabel.setText("Stopped");
    }

    private TimerResponse fetchTimerResponseFromServer() {
        return new TimerResponse(30, true);
    }
}
