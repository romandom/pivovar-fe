package cz.diplomka.pivovarfe.util;


import com.fazecast.jSerialComm.SerialPort;

public class HardwareCommunicator {

    private SerialPort serialPort;

    public void initialize() {
        serialPort = SerialPort.getCommPort("/dev/ttyUSB0");
        serialPort.setBaudRate(9600);
        serialPort.openPort();
    }

    public void sendCommand(String command) {
        if (serialPort.isOpen()) {
            serialPort.writeBytes(command.getBytes(), command.length());
        }
    }

    public String readData() {
        byte[] readBuffer = new byte[1024];
        int numBytes = serialPort.readBytes(readBuffer, readBuffer.length);
        return new String(readBuffer, 0, numBytes);
    }

    public void close() {
        if (serialPort.isOpen()) {
            serialPort.closePort();
        }
    }
}

