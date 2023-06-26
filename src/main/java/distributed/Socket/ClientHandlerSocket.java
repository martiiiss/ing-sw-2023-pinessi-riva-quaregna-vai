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
import java.util.ArrayList;
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
    public void run() {  //METODO PER RICEVERE INPUT DA CLIENT E INVIARE A CLIENT
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (inputLock) {
                    SocketMessage message = receivedMessage();
                    Object obj=null;
                    if(message.getMessageEvent()!=Event.OK) {
                        obj = socketServer.receivedMessage(message);
                    }
                    if(message.getObj() == Event.ASK_MODEL){
                        sendMessage(new SocketMessage(message.getClientIndex(), message.getMatchIndex(), socketServer.receivedMessage(message), message.getMessageEvent()));
                    } else{
                        update(obj, message);
                    }
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

    //SWITCH ERRORI DA SERVER/CONTROLLER
    public void update(Object obj, SocketMessage message) throws IOException, ClassNotFoundException {
        System.out.println("ARRIVA " + message.getClientIndex() + " " + message.getMatchIndex() + " " + message.getObj() + " " +message.getMessageEvent());
        switch (message.getMessageEvent()){
            case OK ->{
                sendMessage(new SocketMessage(clientIndex, matchIndex, obj, Event.SET_CLIENT_INDEX));
            }
            case ASK_NUM_PLAYERS ->{
                this.matchIndex = (int) ((ArrayList)obj).get(0);
                this.clientIndex = (int) ((ArrayList)obj).get(1);
                System.out.println("HO SETTATO GLI INDICI " + matchIndex + clientIndex);
                sendMessage(new SocketMessage(clientIndex, matchIndex, obj, Event.SET_CLIENT_INDEX));
            }
            case CHOOSE_VIEW -> {
                if((Event)obj != Event.GUI_VIEW &&  (Event)obj != Event.TUI_VIEW){
                    System.out.println(((Event)obj).getMsg());
                    sendMessage(new SocketMessage(clientIndex, matchIndex, obj, Event.CHOOSE_VIEW));
                } else{
                    sendMessage(new SocketMessage(clientIndex, matchIndex, null, Event.SET_NICKNAME));
                }
            }
            case SET_NICKNAME -> {
                if((Event)obj != Event.OK){
                    System.out.println(((Event)obj).getMsg());
                    sendMessage(new SocketMessage(clientIndex, matchIndex, obj, Event.SET_NICKNAME));
                } else{
                    do{
                        obj = socketServer.receivedMessage(new SocketMessage(clientIndex, matchIndex, null, Event.ALL_CONNECTED));
                    }while (obj!=Event.OK);
                    System.out.println("pronti a partire!");
                    sendMessage(new SocketMessage(clientIndex, matchIndex, obj, Event.ALL_CONNECTED));
                }
            }
            case GAME_STARTED -> {
                do{
                    obj = socketServer.receivedMessage(new SocketMessage(clientIndex, matchIndex, null, Event.GAME_STARTED));
                }while (obj!=Event.OK);
                sendMessage(new SocketMessage(clientIndex, matchIndex, obj, Event.OK));
            }
        }
    }


    public void sendMessage(SocketMessage message){ //per inviare i messaggi al client
        try {
            output.writeObject(message);
            output.flush();
            output.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SocketMessage receivedMessage() throws IOException, ClassNotFoundException {
        SocketMessage message = (SocketMessage) input.readObject(); //per ricevere i messaggi dal client
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
