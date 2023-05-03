package distributed.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIInterface extends Remote{

    void stampa() throws RemoteException;
    void startServer(ServerRMIInterface serverRmi) throws RemoteException;
    void login(String username, ClientConnectionRMI clientConnection) throws RemoteException;
    void disconnect() throws RemoteException;


    //richiama i metodi del Controller (per il flusso della partita)x\
}
