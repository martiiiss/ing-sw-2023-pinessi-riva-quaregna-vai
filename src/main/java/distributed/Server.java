package distributed;

import controller.Controller;
import distributed.RMI.*;
import distributed.Socket.SocketServer;
import model.Board;
import model.Game;
import util.Event;
import util.Match;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class Server extends UnicastRemoteObject implements Runnable, Remote {
    private int socketPort;
    private int RMIPort;
    private List<Match> matchList;
    //private List<ClientInterface> clientsConnected; //username - connection
    private Object clientsLock;
    private Controller controller ;
    private Game game ;
    private SocketServer serverSocket;
    private ServerRMIInterface rmiServer;

    // private int numOfClientsConnected=0;
    private int maxNumOfClients = 200;

    public Server(int portSocket, int portRMI) throws IOException {
        matchList = new ArrayList<>();
        this.socketPort = portSocket;
        this.RMIPort = RMIPort;
        this.matchList= Collections.synchronizedList(new ArrayList<>());

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



    public ArrayList<Integer> connection (ClientInterface client) throws IOException, ClassNotFoundException {
        /*if(getNumberOfClientsConnected()<maxNumOfClients) {
            if(clientsConnected.size()==1){
                Thread disconnection = new Thread(()-> {
                    startClientStatusCheckTimer();
                }, "Thread disconnection handler");
                disconnection.start();
            }
            this.clientsConnected.add(client);
            System.out.println("Clients: " + getNumberOfClientsConnected());
            return clientsConnected.size() - 1;
        }
        return -1;*/
        Match match = new Match();
        ArrayList<Integer> indexes = new ArrayList<>();
        if(matchList.isEmpty()) {
            System.out.println("No match in list, new match");
            match.addPlayer(client);
            match.setMaxSize(client.askNumOfPlayers());
            match.getGameController().updateController(match.getMaxSize(),Event.ASK_NUM_PLAYERS);
            matchList.add(match);
            indexes.add(matchList.indexOf(match));
            indexes.add((match.getListOfClients().indexOf(client)));
            System.out.println(indexes.get(0)+" "+indexes.get(1));
            System.out.println("Match selected is: "+matchList.indexOf(match));
            startClientStatusCheckTimer();
            return indexes;
        }
        else {
            for(Match matchIterator : matchList) {
                if(matchIterator.getMaxSize()>matchIterator.getListOfClients().size()) {
                    matchIterator.addPlayer(client);
                    System.err.println(matchIterator.getListOfClients().indexOf(client));
                    System.out.println("Match selected is: "+matchList.indexOf(matchIterator));
                    indexes.add(matchList.indexOf(matchIterator));
                    indexes.add((matchIterator.getListOfClients().indexOf(client)));
                    return indexes;
                }
            }
        }
        System.out.println("All Matches are full, new match!");
        match.setMaxSize(client.askNumOfPlayers());
        matchList.add(match);
        match.addPlayer(client);
        System.out.println("Match selected is: "+matchList.indexOf(match));
        indexes.add(matchList.indexOf(match));
        indexes.add((match.getListOfClients().indexOf(client)));
        match.getGameController().updateController(match.getMaxSize(),Event.ASK_NUM_PLAYERS);
        return indexes;
    }
    /*public int returnClientIndex(int matchIndex, ClientInterface client) {
        return matchList.get(matchIndex).getListOfClients().indexOf(client);
    }*/


    private void readyToStart() throws RemoteException{
        //TODO: raggiunto il numero di giocatori necessario la partita può iniziare
    }



    @Override
    public void run() { //throws InterruptedException {
        //TODO
    }

    public Board getServerBoard(){ return this.controller.getBoard();}

    public Event sendServerMessage(int gameIndex, Object obj, Event event) throws IOException {
        //System.out.println(" " + gameIndex + " "  + obj + " " + event);
        if(event==Event.ASK_NUM_PLAYERS)
            matchList.get(gameIndex).getGameController().updateController(matchList.get(gameIndex).getMaxSize(),Event.ASK_NUM_PLAYERS);
        return matchList.get(gameIndex).getGameController().updateController(obj,event);
    }

    public Object getServerModel(int gameIndex, Event event, Object clientIndex) {
        //System.out.println("Event Rec: "+ event+" Match number "+gameIndex);
        return matchList.get(gameIndex).getGameController().getControllerModel(event, clientIndex);
    }
    private boolean clientDisconnected = false;

    public boolean getDisconnections(int matchIndex) {
        return matchList.get(matchIndex).getClientDisconnected();
    }



    private void startClientStatusCheckTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkClientStatus();
            }
        }, 0, 1000);
    }

    private void checkClientStatus() {
            for (Match match : matchList) {
                if (!match.getClientDisconnected()) {
                    Iterator<ClientInterface> iterator = match.getListOfClients().iterator();
                    while (iterator.hasNext()) {
                        ClientInterface client = iterator.next();
                        try {
                            client.ping();
                        } catch (RemoteException e) {
                            match.setClientsConnected();
                        }
                    }
                }
            }
    }
}