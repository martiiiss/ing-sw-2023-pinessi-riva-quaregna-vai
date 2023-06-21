package distributed.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void ping() throws RemoteException;

}
