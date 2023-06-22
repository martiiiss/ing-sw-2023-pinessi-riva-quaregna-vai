package distributed.RMI;

import distributed.Client;
import model.Board;
import util.Event;
import util.Event;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerRMIInterface extends Remote {

    void startServer(ServerRMIInterface serverRmi) throws RemoteException;
    // void login(String username, ClientConnectionRMI clientConnection) throws RemoteException;

    ArrayList<Integer> initClient(ClientInterface rmiClient) throws IOException, AlreadyBoundException;

    //boolean getNumberOfPlayer(int num) throws IOException;

    Event sendMessage(int gameIndex, Object obj, Event event) throws IOException;

    Object getModel(int gameIndex, Event event, Object clientIndex) throws RemoteException;
   // int getClientIndex(int matchIndex, ClientInterface client);
    boolean getDisconnection(int matchIndex) throws RemoteException;

    //richiama i metodi del Controller (per il flusso della partita)x\
}
