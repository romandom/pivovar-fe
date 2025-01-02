package cz.diplomka.pivovarfe.model;

public class TimerResponse {
    private final int timeInSeconds;
    private final boolean continueTimer;

    public TimerResponse(int timeInSeconds, boolean continueTimer) {
        this.timeInSeconds = timeInSeconds;
        this.continueTimer = continueTimer;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public boolean isContinueTimer() {
        return continueTimer;
    }
}
