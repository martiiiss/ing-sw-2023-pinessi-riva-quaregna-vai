package distributed.Socket;

import distributed.ClientInterface;
import distributed.Server;
import distributed.messages.Message;
import distributed.messages.SocketMessage;
import util.Event;
import view.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class SocketServer extends UnicastRemoteObject implements Runnable{
    private final Server server;
    private final int port;
    private ServerSocket serverSocket;
    private static List<ClientHandlerSocket> clients = new ArrayList<>();
    private static List<View> clientsView = new ArrayList<>();
    private int numOfClients;

    public SocketServer(int port, Server server) throws IOException {
        this.server = server;
        this.port = port;
    }

    @Override
    public void run()  {  //METODO CHE ATTENDE LA CONNESSIONE DI CLIENT
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            //System.err.println(e.getMessage());
            return;
        }
        System.out.println("ServerSocket ready on port: "+port);
        while(!Thread.currentThread().isInterrupted()){
            try{
                Socket socketClient = serverSocket.accept(); //client socket
                socketClient.setSoTimeout(0); //inf
                ClientHandlerSocket clientHandlerSocket = new ClientHandlerSocket(socketClient, this);
                View view = new View(clientHandlerSocket);
                clients.add(clientHandlerSocket);
                clientsView.add(view);
                Thread clientThread = new Thread(clientHandlerSocket);
                clientThread.start();
                System.out.println("un nuovo client si è connesso!");
            } catch (IOException e){
               // e.printStackTrace();
            }
        }

    }

    public synchronized Object askMyIndex () throws IOException, ClassNotFoundException {
        ArrayList<Integer> indexFromServer = server.connection((ClientInterface) clients.get(clients.size()-1));
        return indexFromServer;
    }

    private static void broadcastMessage(SocketMessage message){
        for(ClientHandlerSocket client : clients){
            client.sendMessage(message);
        }
    }


    public Object receivedMessage(SocketMessage message) throws IOException, ClassNotFoundException {
        Object obj;
        if(message.getObj()==Event.CHECK_MY_TURN){
            Object indexPit = server.getServerModel(message.getMatchIndex(), message.getMessageEvent(), message.getClientIndex());
            return indexPit;
        } else{
            if(message.getObj()==Event.ASK_MODEL){
                obj = server.getServerModel(message.getMatchIndex(), message.getMessageEvent(), message.getClientIndex());
            } else if(message.getMessageEvent()==Event.ASK_NUM_PLAYERS){
                obj = askMyIndex();
            } else {
                obj = server.sendServerMessage(message.getMatchIndex(), message.getObj(), message.getMessageEvent());
            }
            // server return an Error
            return obj;
            //updateView(message, clientIndex, matchIndex);
        }
    }

    public int askPit(int matchIndex) {
        return server.askPitController(matchIndex);
    }


    public void updateView(Message message, int clientIndex, int matchIndex) throws IOException {
        clientsView.get(clientIndex).ask(message); //TODO: devo gestire una lista di client co myIndex e matchIndex
    }
    public void onDisconnect(int matchIndex) {
        server.onDisconnect(matchIndex);
    }
    public boolean askDisconnection(int matchIndex) {
        return server.askMatchDisconnection(matchIndex);
    }
}

