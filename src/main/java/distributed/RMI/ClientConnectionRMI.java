package distributed.RMI;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientConnectionRMI extends Remote {
    void firstClientMessages() throws IOException;

    void disconnected();

    void ping();
    void disconnect();
}
