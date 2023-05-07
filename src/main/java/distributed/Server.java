package distributed;

import controller.Controller;
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
import java.util.HashMap;
import java.util.Map;

public class Server extends UnicastRemoteObject implements Runnable, Remote {
    private int socketPort;
    private int RMIPort;
    private ArrayList<Client> clientsConnected = new ArrayList<>(); //username - connection
    private Object clientsLock;
    private Controller controller = new Controller();
    private Game game = controller.getInstanceOfGame();
    private SocketServer serverSocket;
    private ServerRMIInterface rmiServer;
    public Server(int portSocket, int portRMI) throws RemoteException {
        super();
        this.socketPort = portSocket;
        this.RMIPort = portRMI;
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
        int numberOfPlayers;
        clientsConnected.add(client);
        if(clientsConnected.size()==1) {
            askClientNumber(clientsConnected.get(0));
            controller.createGame();
        }
        controller.chooseNickname(this);
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

    public void update(Client client, Object o, Event event) throws IOException {
        controller.update(client,o,event);
    }
    Server server = this;
    public Server getInstanceOfServer() {return server;}

    public Object request(Event event) throws IOException {
        return clientsConnected.get(clientsConnected.size()-1).request(event);
    }
}
