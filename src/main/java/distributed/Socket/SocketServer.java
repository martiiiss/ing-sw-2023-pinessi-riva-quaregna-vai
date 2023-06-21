package distributed.Socket;

import distributed.Server;
import distributed.messages.Message;
import util.Event;
import view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer extends Server implements Runnable{
    private final Server server;
    private final int port;
    private ServerSocket serverSocket;
    private static List<ClientHandlerSocket> clients = new ArrayList<>();
    private static List<View> clientsView = new ArrayList<>();


    public SocketServer(int port) throws IOException {
        super(port, -1);
        this.server = super.getInstanceOfServer();
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


    private static void broadcastMessage(Message message){
        for(ClientHandlerSocket client : clients){
            client.sendMessage(message);
        }
    }

    public void receivedMessage(Message message) throws IOException {
        System.out.println("messaggio ricevuto dal server" + message.getMessageEvent());
        //server.sendServerMessage(message.getObj(), message.getMessageEvent());
        // server return an Error
        updateView(message);
    }


    public void updateView(Message message){
        System.out.println("messaggio in socjet server update view: " + message.getMessageEvent());
        switch(message.getMessageEvent()){
            case SET_NICKNAME -> {
                clientsView.get(0).ask();
            }
        }
    }
}

