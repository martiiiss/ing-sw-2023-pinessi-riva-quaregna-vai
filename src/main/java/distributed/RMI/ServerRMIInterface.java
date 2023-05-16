package distributed.RMI;

import distributed.Client;
import model.Board;
import util.Error;
import util.Event;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIInterface extends Remote{

    void startServer(ServerRMIInterface serverRmi) throws RemoteException;
    // void login(String username, ClientConnectionRMI clientConnection) throws RemoteException;

    void initClient(Client rmiClient) throws IOException;

    //boolean getNumberOfPlayer(int num) throws IOException;
    int getNumberOfConnections() throws RemoteException;
    Error onEventInserted(Object obj, Event event, int numPlayer) throws IOException;

    Event sendMessage(int num) throws RemoteException;
    //richiama i metodi del Controller (per il flusso della partita)x\

    Board getBoard() throws RemoteException;
}
