package distributed.RMI;

import controller.Controller;
import distributed.Client;
import distributed.Server;
import util.Event;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerRMIInterface extends Remote{

    void startServer(ServerRMIInterface serverRmi) throws RemoteException;
    // void login(String username, ClientConnectionRMI clientConnection) throws RemoteException;
    void disconnect() throws RemoteException;

    void initClient(Client rmiClient) throws IOException;

    boolean getNumberOfPlayer(int num) throws IOException;
    int getNumberOfConnections() throws RemoteException;
    void sendUpdate(Client client, Object o , Event event) throws IOException;

    //richiama i metodi del Controller (per il flusso della partita)x\
}
