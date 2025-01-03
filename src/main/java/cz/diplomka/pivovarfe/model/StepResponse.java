package cz.diplomka.pivovarfe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StepResponse {
    @JsonProperty("timeInSeconds")
    private int timeInSeconds;
    @JsonProperty("continueTimer")
    private boolean continueTimer;
    @JsonProperty("targetTempMash")
    private double targetTempMash;
    @JsonProperty("targetTempWorth")
    private double targetTempWorth;

    public StepResponse() {}

    public StepResponse(int timeInSeconds, boolean continueTimer, double targetTempMash, double targetTempWorth) {
        this.timeInSeconds = timeInSeconds;
        this.continueTimer = continueTimer;
        this.targetTempMash = targetTempMash;
        this.targetTempWorth = targetTempWorth;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public boolean isContinueTimer() {
        return continueTimer;
    }

    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public void setContinueTimer(boolean continueTimer) {
        this.continueTimer = continueTimer;
    }

    public double getTargetTempMash() {
        return targetTempMash;
    }

    public void setTargetTempMash(double targetTempMash) {
        this.targetTempMash = targetTempMash;
    }

    public double getTargetTempWorth() {
        return targetTempWorth;
    }

    public void setTargetTempWorth(double targetTempWorth) {
        this.targetTempWorth = targetTempWorth;
    }
}
