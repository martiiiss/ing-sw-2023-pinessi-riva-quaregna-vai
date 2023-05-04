package distributed.RMI;

import controller.Controller;
import distributed.Client;
import distributed.Server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerRMIInterface extends Remote{

    void startServer(ServerRMIInterface serverRmi) throws RemoteException;
   // void login(String username, ClientConnectionRMI clientConnection) throws RemoteException;
    void disconnect() throws RemoteException;

    void initClient(Client rmiClient) throws RemoteException;

    int getNumberOfConnections() throws RemoteException;

    void askNumOfPlayers()  throws IOException, RemoteException;
    //richiama i metodi del Controller (per il flusso della partita)x\
}
