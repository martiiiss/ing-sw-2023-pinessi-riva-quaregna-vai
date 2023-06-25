package distributed.Socket;

import distributed.Client;
import distributed.RMI.ClientInterface;
import distributed.messages.Message;
import distributed.messages.SocketMessage;
import util.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientHandlerSocket implements Runnable, ClientInterface {
    private Socket socketClient;
    private SocketServer socketServer;
    private final Object inputLock;
    private final Object outputLock;
    private ObjectOutputStream output; //per inviare
    private ObjectInputStream input; //per ricevere
    private Object inputObject;
    private int clientIndex;
    private int matchIndex;
    private int numOfPlayers;

    public ClientHandlerSocket(Socket socket, SocketServer socketServer) {
        this.socketServer = socketServer;
        this.socketClient = socket;

        this.inputLock = new Object();
        this.outputLock = new Object();

        try{
            this.output = new ObjectOutputStream(socketClient.getOutputStream());
            this.input = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (inputLock) {
                    System.out.println("ricevi messaggio: ");
                    SocketMessage message = receivedMessage();
                    if(message.getMessageEvent()==Event.ASK_NUM_PLAYERS){
                        numOfPlayers = (int)message.getObj();
                    }
                    socketServer.receivedMessage(message);
                    sendMessage(new SocketMessage(clientIndex, matchIndex, "ricevuto ! ", Event.GAME_CGC));
                }
            }
        }catch(ClassCastException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        try {
            socketClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(SocketMessage message){ //per inviare i messaggi al client
        try {
            System.out.println("in send mess " + message.getMessageEvent());
            output.writeObject(message);
            output.flush();
            output.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SocketMessage receivedMessage() throws IOException, ClassNotFoundException {
        SocketMessage message = (SocketMessage) input.readObject(); //per ricevere i messaggi dal client
        this.inputObject = message.getObj();
        if(this.inputObject!=null) {
            String temp = this.inputObject.toString();
            System.out.println("ogg ricevuto " + temp + " da " + socketClient.getInetAddress());
        }
        return message;
    }


    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public int askNumOfPlayers() throws IOException, ClassNotFoundException {
        sendMessage(new SocketMessage(clientIndex, matchIndex, numOfPlayers, Event.ASK_NUM_PLAYERS));
        return (int)receivedMessage().getObj();
    }
}
