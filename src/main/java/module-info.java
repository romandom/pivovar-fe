module cz.diplomka.pivovarfe {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires spring.websocket;
    requires spring.messaging;

    opens cz.diplomka.pivovarfe to javafx.fxml;
    exports cz.diplomka.pivovarfe;
    exports cz.diplomka.pivovarfe.controller;
    opens cz.diplomka.pivovarfe.controller to javafx.fxml;
    opens cz.diplomka.pivovarfe.model to com.fasterxml.jackson.databind;
    exports cz.diplomka.pivovarfe.constant;
}