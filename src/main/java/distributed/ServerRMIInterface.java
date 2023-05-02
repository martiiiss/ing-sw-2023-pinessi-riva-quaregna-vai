package distributed;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIInterface extends Remote{
    void startServer();
    void login(String username, ClientConnectionRMI clientConnection) throws RemoteException;
    void disconnect();

    //richiama i metodi del Controller (per il flusso della partita)x\
}
