package distributed.RMI;
import distributed.RMI.ClientConnectionRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIInterface extends Remote{
    void startServer(RMIServer rmiServer) throws RemoteException;
    void login(String username, ClientConnectionRMI clientConnection) throws RemoteException;
    void disconnect() throws RemoteException;

    //richiama i metodi del Controller (per il flusso della partita)x\
}
