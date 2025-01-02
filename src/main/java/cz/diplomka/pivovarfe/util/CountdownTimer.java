package cz.diplomka.pivovarfe.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

public class CountdownTimer {

    private final int initialSeconds;
    private int remainingSeconds;
    private final Timeline timeline;
    private final Consumer<Integer> onTick;
    private final Runnable onFinish;

    public CountdownTimer(int initialSeconds, Consumer<Integer> onTick, Runnable onFinish) {
        this.initialSeconds = initialSeconds;
        this.remainingSeconds = initialSeconds;
        this.onTick = onTick;
        this.onFinish = onFinish;

        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (remainingSeconds > 0) {
                remainingSeconds--;
                onTick.accept(remainingSeconds);
            } else {
                stop();
                onFinish.run();
            }
        }));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    public void reset() {
        stop();
        remainingSeconds = initialSeconds;
    }
}

