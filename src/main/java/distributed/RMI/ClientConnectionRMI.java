package distributed.RMI;

import java.rmi.Remote;

public interface ClientConnectionRMI extends Remote {
    void messageReceived();

    void disconnected();

    void ping();
    void disconnect();
}
