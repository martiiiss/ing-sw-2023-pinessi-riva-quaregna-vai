package distributed.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback{
    void onClientDisconnected() throws RemoteException;
}
