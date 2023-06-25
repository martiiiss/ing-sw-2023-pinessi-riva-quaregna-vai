package distributed.Socket;

import distributed.RMI.ClientInterface;
import distributed.Server;
import distributed.messages.Message;
import distributed.messages.SocketMessage;
import util.Event;
import view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public void run()  {
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("ServerSocket ready on port: "+port);
        while(!Thread.currentThread().isInterrupted()){
            try{
                System.out.println("attendo client...");
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
                e.printStackTrace();
            }
        }

    }

    public void askMyIndex () throws IOException, ClassNotFoundException {
        ArrayList<Integer> indexFromServer = server.connection((ClientInterface) clients.get(clients.size()-1));
        clients.get(clients.size()-1).sendMessage(new SocketMessage(indexFromServer.get(1), indexFromServer.get(0), numOfClients, Event.SET_CLIENT_INDEX));
        System.out.println("MY INDEX " + indexFromServer.get(1) + " MY MATCH "+ indexFromServer.get(0));
    }

    private static void broadcastMessage(SocketMessage message){
        for(ClientHandlerSocket client : clients){
            client.sendMessage(message);
        }
    }

    public void receivedMessage(SocketMessage message) throws IOException, ClassNotFoundException {
        System.out.println("Il server ha ricevuto " + message.getMessageEvent() + message.getObj());
        if(message.getMessageEvent()==Event.ASK_NUM_PLAYERS){
            numOfClients = (int)message.getObj();
            askMyIndex();
        } else {
            server.sendServerMessage(message.getMatchIndex(), message.getObj(), message.getMessageEvent());
        }
        // server return an Error

        //updateView(message, clientIndex, matchIndex);
    }


    public void updateView(Message message, int clientIndex, int matchIndex) throws IOException {
        System.out.println("messaggio in socjet server update view: " + message.getMessageEvent());
        clientsView.get(clientIndex).ask(message); //TODO: devo gestire una lista di client co myIndex e matchIndex
    }
}

