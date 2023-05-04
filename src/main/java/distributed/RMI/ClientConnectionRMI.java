package distributed.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientConnectionRMI extends Remote {
    void messageReceived() throws RemoteException;

    void disconnected();

    void ping();
    void disconnect();
}
