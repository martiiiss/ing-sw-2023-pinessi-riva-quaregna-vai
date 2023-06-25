package distributed.RMI;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void ping() throws RemoteException;
    int askNumOfPlayers() throws IOException, ClassNotFoundException;
}
