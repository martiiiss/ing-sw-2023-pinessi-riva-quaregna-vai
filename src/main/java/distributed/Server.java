package distributed;

import controller.Controller;
import distributed.RMI.RMIClient;
import distributed.RMI.RMIServer;
import distributed.RMI.ServerRMIInterface;
import distributed.Socket.SocketServer;
import model.Game;
import model.Player;
import util.Event;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import util.Callback;

public class Server extends UnicastRemoteObject implements Runnable, Remote {
    private int socketPort;
    private int RMIPort;
    private ArrayList<Client> clientsConnected = new ArrayList<>(); //username - connection
    private Object clientsLock;
    private Controller controller = new Controller();
    private Game game = controller.getInstanceOfGame();
    private SocketServer serverSocket;
    private ServerRMIInterface rmiServer;

    private int numOfClientsConnected=0;


    public Server(int portSocket, int portRMI) throws IOException {
        super();
        this.socketPort = portSocket;
        this.RMIPort = portRMI;
        controller.createGame();
    }

    public Controller getInstanceOfController(){
        return controller;
    }

    public SocketServer getInstanceOfSocketServer(){
        return serverSocket;
    }

    public ServerRMIInterface getInstanceOfRMIServer(){
        return rmiServer;
    }


    public void startServers(int protocol) throws IOException { // fa partire server socket e rmi
        if(protocol ==1){
            serverSocket = new SocketServer(this, socketPort);
            serverSocket.startServer(serverSocket);
        } else if (protocol == 2){
            rmiServer = new RMIServer(this, RMIPort);
            rmiServer.startServer(rmiServer);
        }
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

    public void connection (Client client) throws IOException {
        this.clientsConnected.add(client);
        this.numOfClientsConnected++;
        System.out.println("size "+ this.clientsConnected.size());
    }

    public int getNumberOfClientsConnected() {
        System.out.println("non stampa lo stesso numero " + this.numOfClientsConnected);
        return this.numOfClientsConnected;
    }

    private void askClientNumber(Client firstClient) {

    }

    public boolean setClientNumber(int num) throws IOException {
        if(controller.chooseNumOfPlayer(num)){
            return true;
        } else
            return false;
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

    public String getClientsConnected() {
        return String.valueOf(clientsConnected.size());
    }



    public boolean getUpdates(Object obj, Event event) throws IOException {
        return controller.update(obj, event, clientsConnected.size()); //passa oggetto restituito da view e evento attuale al controller
    }
    Server server = this;
    public Server getInstanceOfServer() {return server;}
}
