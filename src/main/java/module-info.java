module cz.diplomka.pivovarfe {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires static lombok;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;


    opens cz.diplomka.pivovarfe to javafx.fxml;
    exports cz.diplomka.pivovarfe;
    exports cz.diplomka.pivovarfe.controller;
    opens cz.diplomka.pivovarfe.controller to javafx.fxml;
}