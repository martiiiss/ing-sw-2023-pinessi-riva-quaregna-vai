package distributed;

import controller.Controller;
import distributed.RMI.RMIServer;
import distributed.RMI.ServerRMIInterface;
import distributed.Socket.SocketServer;
import model.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server extends UnicastRemoteObject implements Runnable, Remote {
    private int socketPort;
    private int RMIPort;
    private ArrayList<Client> clientsConnected = new ArrayList<>(); //username - connection
    private Object clientsLock;
    private Controller controller = new Controller();
    private Game game = controller.getInstanceOfGame();
    public Server(int port, int protocol) throws RemoteException {
        super(port);
        if(protocol == 1){
            this.socketPort = port;
        } else if(protocol == 2){
            this.RMIPort = port;
        }
    }

    public Controller getInstanceOfController(){
        return controller;
    }


    private void startServers() throws RemoteException{ // fa partire server socket e rmi
        SocketServer serverSocket = new SocketServer();
        serverSocket.startServer();

        ServerRMIInterface rmiServer = new RMIServer(this, RMIPort);
        rmiServer.startServer(rmiServer);
    }

    /*public void login(String username, Connection connection) throws RemoteException{
        if(clients.containsKey(username)){
            reconnectionOfPlayer(username, connection);
        } else{
            firstConnection(username, connection);
        }
        //TODO registra un client (utente) in partita
    }*/

    private void reconnectionOfPlayer(String username, Connection connection) throws RemoteException{
        //TODO riconnette un giocatore (funzionalià aggiuntive)
    }

    public void connection (Client client) throws RemoteException{
        clientsConnected.add(client);
        System.out.println("Successfully added"+clientsConnected.get(0).getUsername());
        System.out.println(clientsConnected.size());
        }

        //TODO



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

    public String getClientsConnected() {
        return String.valueOf(clientsConnected.size());
    }
}
