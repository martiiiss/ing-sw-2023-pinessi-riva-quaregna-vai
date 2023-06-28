package distributed.Socket;

import distributed.ClientInterface;
import distributed.messages.SocketMessage;
import util.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static util.Event.*;

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
    private Thread threadAskPit;
    private Object lock;
    private boolean first = true;
    private int pit;
    public ClientHandlerSocket(Socket socket, SocketServer socketServer) {
        this.socketServer = socketServer;
        this.socketClient = socket;

        this.lock = new Object();
        this.inputLock = new Object();
        this.outputLock = new Object();

        try{
            this.output = new ObjectOutputStream(socketClient.getOutputStream());
            this.input = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        threadAskPit = new Thread(()-> {
            synchronized (lock) {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        askPitController();
                    } catch (IOException | ClassNotFoundException | InterruptedException e) {
                        //throw new RuntimeException(e);
                    }
                }
            }
        },"AskPITToController");
    }
    private void askPitController() throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("askPIT!!!!!");
        do {

            pit = socketServer.askPit(matchIndex);
            System.out.println("heow " + clientIndex + " " + pit);
            if(first && pit!=clientIndex){
                first=false;
                System.out.println("pit diverso" + pit + "cl " + clientIndex);
                sendMessage(new SocketMessage(clientIndex, matchIndex, pit, Event.NOT_YOUR_TURN));
            }
            Thread.sleep(1000);
        } while(pit !=clientIndex);
        first = true;
        System.out.println("manda messaggio è il tuo turno! " + clientIndex);
        sendMessage(new SocketMessage(clientIndex, matchIndex, pit, Event.START_YOUR_TURN));
        lock.wait();
    }

    @Override
    public void run() {  //METODO PER RICEVERE INPUT DA CLIENT E INVIARE A CLIENT
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (inputLock) {
                    SocketMessage message = receivedMessage();
                    //System.out.println("ricevo " + message.getObj() + " ev " + message.getMessageEvent());
                    Object obj=null;
                    if(message.getMessageEvent() == START_THREAD) {
                        if (!threadAskPit.isAlive()) {
                            System.out.println("STARTA IL THR");
                            threadAskPit.start();
                        }
                    }

                    if(message.getObj()==Event.CHECK_MY_TURN){
                        obj = socketServer.receivedMessage(message);
                        int index=(int)obj;
                        if(index== message.getClientIndex()){
                            sendMessage(new SocketMessage(message.getClientIndex(), message.getMatchIndex(), index, Event.START_YOUR_TURN));
                        } else{
                            sendMessage(new SocketMessage(message.getClientIndex(), message.getMatchIndex(), index, Event.NOT_YOUR_TURN));
                        }

                    } else  if(message.getMessageEvent() == END_OF_TURN && message.getObj()==END_OF_TURN){
                        System.out.println("");
                       // sendMessage(new SocketMessage(clientIndex, matchIndex, null, NOT_YOUR_TURN));
                        System.out.println("non è il tuo turno");
                        synchronized (lock) {
                            this.lock.notifyAll();
                        }
                    } else {
                        if (message.getMessageEvent() != Event.OK) {
                            obj = socketServer.receivedMessage(message);
                        }
                        if (message.getObj() == Event.ASK_MODEL) {
                            sendMessage(new SocketMessage(message.getClientIndex(), message.getMatchIndex(), obj, message.getMessageEvent()));
                        } else {
                            update(obj, message);
                        }
                    }
                }
            }
        }catch(ClassCastException | ClassNotFoundException | IOException e) {
            System.out.println("");
        }
        try {
            socketClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //SWITCH ERRORI DA SERVER/CONTROLLER
    public void update(Object obj, SocketMessage message) throws IOException, ClassNotFoundException {
        switch (message.getMessageEvent()){
            case OK ->{
                sendMessage(new SocketMessage(clientIndex, matchIndex, obj, Event.SET_CLIENT_INDEX));
            }
            case ASK_NUM_PLAYERS ->{
                this.matchIndex = (int) ((ArrayList)obj).get(0);
                this.clientIndex = (int) ((ArrayList)obj).get(1);
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
                        obj = socketServer.receivedMessage(new SocketMessage(clientIndex, matchIndex, obj, Event.ALL_CONNECTED));
                    }while (obj!=Event.OK);
                    sendMessage(new SocketMessage(clientIndex, matchIndex, obj, Event.ALL_CONNECTED));
                }
            }
            case GAME_STARTED -> {
                do{
                    obj = socketServer.receivedMessage(new SocketMessage(clientIndex, matchIndex, null, Event.GAME_STARTED));
                }while (obj!=Event.OK);
                sendMessage(new SocketMessage(clientIndex, matchIndex, obj, Event.OK));
            }
            case TURN_AMOUNT, TURN_PICKED_TILES, TURN_COLUMN, TURN_POSITION, CHECK_REFILL, END_OF_TURN, CHECK_MY_TURN, SET_UP_BOARD -> {
                sendMessage(new SocketMessage(clientIndex, matchIndex, obj, message.getMessageEvent()));
            }


        }
    }


    public void sendMessage(SocketMessage message){ //per inviare i messaggi al client
        try {
            output.writeObject(message);
            output.flush();
            output.reset();
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
    }

    public SocketMessage receivedMessage() throws IOException, ClassNotFoundException {
        Object o = input.readObject();
        SocketMessage message = (SocketMessage) o ; //per ricevere i messaggi dal client
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
