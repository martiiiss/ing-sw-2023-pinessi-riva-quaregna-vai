package distributed.RMI;

public interface ClientConnectionRMI {
    void messageReceived();
    void ping();
    void disconnect();
}
