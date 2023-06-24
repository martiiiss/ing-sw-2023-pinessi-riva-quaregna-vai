package distributed.Socket;

import distributed.messages.Message;
import util.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandlerSocket implements Runnable {
    private Socket socketClient;
    private SocketServer socketServer;
    private final Object inputLock;
    private final Object outputLock;
    private ObjectOutputStream output; //per inviare
    private ObjectInputStream input; //per ricevere
    private Object inputObject;

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
                    Message message = receivedMessage();
                    //TODO: socketServer.metodo -> per utilizzare i metodi del ScocketServer
                    socketServer.receivedMessage(message);
                    sendMessage("ricevuto ! ");
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

    public void sendMessage(Object obj){ //per inviare i messaggi al client
        try {
            System.out.println("in send mess " + obj);
            output.writeObject(obj);
            output.flush();
            output.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Message receivedMessage() throws IOException, ClassNotFoundException {
        this.inputObject =  (Message)input.readObject(); //per ricevere i messaggi dal client
        if(this.inputObject!=null) {
            String temp = ((Message) this.inputObject).getObj().toString();
            System.out.println("ogg ricevuto " + temp + " da " + socketClient.getInetAddress());
        }
        return (Message) this.inputObject;
    }




}
