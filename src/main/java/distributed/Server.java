package distributed;

import distributed.Socket.Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class Server extends UnicastRemoteObject implements Runnable, Remote {
    private int socketPort;
    private int RMIPort;
    private Map<String, Connection> clients; //username - connection
    private Object clientsLock;


    public Server(int port) throws RemoteException {
        super(port);
        //TODO
    }
    private void startServers() throws RemoteException{
        //TODO fa partire server socket e rmi
    }

    public void login(String username, Connection connection) throws RemoteException{
        //TODO registra un client (utente) in partita
    }

    private void reconnectionOfPlayer(String username, Connection connection) throws RemoteException{
        //TODO riconnette un giocatore (funzionalià aggiuntive)
    }

    private void firstConnection (String username, Connection connection) throws RemoteException{
        //TODO
    }

    private void readyToStart() throws RemoteException{
        //TODO: raggiunto il numero di giocatori necessario la partita può iniziare
    }

    public void playerDisconnected(Connection connection) throws RemoteException{
        //TODO: avvisa utenti che un giocatore si è disconnesso
    }

    //TODO: metodi sendMessage e getMessage

    @Override
    public void run() { //throws InterruptedException {
        //TODO
    }
}
