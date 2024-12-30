package cz.diplomka.pivovarfe.websocket;

import javafx.application.Platform;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class WebSocketClient {

    private final String url;
    private final Consumer<String> onTemperatureUpdate;
    private final Consumer<String> onError;

    private StompSession stompSession;

    public WebSocketClient(String url, Consumer<String> onTemperatureUpdate, Consumer<String> onError) {
        this.url = url;
        this.onTemperatureUpdate = onTemperatureUpdate;
        this.onError = onError;
    }

    public void connect() {
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new StringMessageConverter());

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();

        CompletableFuture<StompSession> futureSession = stompClient.connectAsync(url, handshakeHeaders, connectHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                stompSession = session;
                subscribeToTopics();
            }
        });

        futureSession.whenComplete((session, throwable) -> {
            if (throwable != null) {
                Platform.runLater(() -> onError.accept("Disconnected: " + throwable.getMessage()));
            } else {
                stompSession = session;
            }
        });
    }

    private void subscribeToTopics() {
        if (stompSession != null) {
            stompSession.subscribe("/topic/temperature", new StompSessionHandlerAdapter() {
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    Platform.runLater(() -> onTemperatureUpdate.accept(payload.toString()));
                }
            });

            stompSession.subscribe("/topic/errors", new StompSessionHandlerAdapter() {
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    Platform.runLater(() -> onError.accept(payload.toString()));
                }
            });
        }
    }
}
