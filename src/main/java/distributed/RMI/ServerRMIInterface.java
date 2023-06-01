package distributed.RMI;

import distributed.Client;
import model.Board;
import util.Event;
import util.Event;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIInterface extends Remote {

    void startServer(ServerRMIInterface serverRmi) throws RemoteException;
    // void login(String username, ClientConnectionRMI clientConnection) throws RemoteException;

    int initClient(Client rmiClient) throws IOException;

    //boolean getNumberOfPlayer(int num) throws IOException;
    int getNumberOfConnections() throws RemoteException;

    Event sendMessage(Object obj, Event event) throws IOException;

    Object getModel(Event event, Object clientIndex) throws RemoteException;

    //richiama i metodi del Controller (per il flusso della partita)x\
}
