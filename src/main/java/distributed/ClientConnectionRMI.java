package distributed;

public interface ClientConnectionRMI {
    void messageReceived();
    void ping();
    void disconnect();
}
