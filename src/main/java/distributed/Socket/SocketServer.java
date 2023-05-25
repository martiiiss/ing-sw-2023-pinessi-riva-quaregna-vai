package distributed.Socket;

import distributed.Server;
import distributed.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer extends Server implements Runnable{
    private final Server server;
    private final int port;
    private ServerSocket serverSocket;
    private static List<ClientHandlerSocket> clients = new ArrayList<>();


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
                clients.add(clientHandlerSocket);
                Thread clientThread = new Thread(clientHandlerSocket);
                clientThread.start();
                System.out.println("un nuovo client si è connesso!");
            } catch (IOException e){
                e.printStackTrace();
            }
        }

    }
/*
    public void sendMessage(Object obj){
        try {
            output.writeObject(obj);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

 */

    private static void broadcastMessage(Object obj){
        for(ClientHandlerSocket client : clients){
            client.sendMessage(obj);
        }
    }

    public void receivedMessage(Object message){
        System.out.println(message);
    }
}
