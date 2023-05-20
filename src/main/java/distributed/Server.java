package distributed;

import controller.Controller;
import distributed.RMI.RMIClient;
import distributed.RMI.RMIServer;
import distributed.RMI.ServerRMIInterface;
import distributed.Socket.SocketServer;
import model.Board;
import model.Game;
import util.Error;
import util.Event;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class Server extends UnicastRemoteObject implements Runnable, Remote {
    private int socketPort;
    private int RMIPort;
    private List<Client> clientsConnected; //username - connection
    private Object clientsLock;
    private Controller controller ;
    private Game game ;
    private SocketServer serverSocket;
    private ServerRMIInterface rmiServer;

    // private int numOfClientsConnected=0;

    public List<Client> getClientsConnectedList(){
        return this.clientsConnected;
    }

    public Server(int portSocket, int portRMI) throws IOException {
        if(portRMI==-1) { //Socket
            //    System.out.println("socket");
            this.socketPort = portSocket;
        } else if(portSocket == -1){
            //     System.out.println("rmi");
            this.RMIPort = portRMI;
        }

        //System.out.println("entrambi");
        //this.socketPort = portSocket;
        //this.RMIPort = RMIPort;
        this.controller= new Controller();
        this.game = controller.getInstanceOfGame();
        this.clientsConnected=new ArrayList<>();
        System.out.println("\u001B[32mServer Ready! \u001B[0m");
    }

    public Server getInstanceOfServer(){ return this; }
    public Controller getInstanceOfController(){
        return controller;
    }

    public SocketServer getInstanceOfSocketServer(){
        return serverSocket;
    }

    public ServerRMIInterface getInstanceOfRMIServer(){
        return rmiServer;
    }


    private void reconnectionOfPlayer(String username, Connection connection) throws RemoteException{ //FIXME non può richiedere come parametro il nickname dato che viene richiesto solo dopo che si è connesso        //TODO
    }

    public int connection (Client client) throws IOException {
        this.clientsConnected.add(client);
        return clientsConnected.size()-1;
    }

    public int getNumberOfClientsConnected() {
        /*for(Client c: clientsConnected){
            System.out.println("TESTING "+c.getUsername());
        }
        System.out.println("non stampa lo stesso numero " + this.clientsConnected.size());*/
        //   return this.numOfClientsConnected;
        return this.clientsConnected.size();
    }

    private void readyToStart() throws RemoteException{
        //TODO: raggiunto il numero di giocatori necessario la partita può iniziare
    }

    public void playerDisconnected(Connection connection) throws RemoteException{
        //TODO: avvisa utenti che un giocatore si è disconnesso
    }

    @Override
    public void run() { //throws InterruptedException {
        //TODO
    }

    public int getClientsConnected() {
        return this.clientsConnected.size();
    }

    public boolean isEveryoneConnected() {
        if(clientsConnected.size()==controller.getInstanceOfGame().getNumOfPlayers()) {
            System.err.println("Everyone is now connected!");
            return true;
        }
        else if(clientsConnected.size()<controller.getInstanceOfGame().getNumOfPlayers()) {
            System.err.println("Number of players not yet reached");
            return false;
        }
        System.err.println("DI AL CLIENT CHE CI SONO GIA' TUTTI I PLAYERS CONNESSI...");
        clientsConnected.remove(clientsConnected.get(clientsConnected.size()-1));
        return false;
    }


    public Board getServerBoard(){ return this.controller.getBoard();}

    public Error sendServerMessage(Object obj, Event event) throws IOException {
        return controller.updateController(obj,event);
    }

    public Object getServerModel(Event event, Object clientIndex) {
        return controller.getControllerModel(event, clientIndex);
    }
}