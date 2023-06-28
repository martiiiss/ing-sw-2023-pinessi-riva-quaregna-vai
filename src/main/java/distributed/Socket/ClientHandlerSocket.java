package distributed.Socket;

import distributed.ClientInterface;
import distributed.messages.SocketMessage;
import model.Board;
import org.jetbrains.annotations.NotNull;
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

/**Class that represents the communication in between the socket client and its server*/
public class ClientHandlerSocket implements Runnable, ClientInterface {
    private Socket socketClient;
    private SocketServer socketServer;
    private final Object inputLock;
    private final Object outputLock;
    private ObjectOutputStream output; //used to send
    private ObjectInputStream input; //used to receive
    private Object inputObject;
    private int clientIndex;
    private int matchIndex;
    private int numOfPlayers;
    private Thread threadAskPit;
    private Thread threadAskDisconnection;
    private Thread threadCheckUpdates;
    private Object lock;
    private Object upLock;
    private boolean first = true;
    private int pit;
    private boolean stoppati;

    /**Constructor of the Class. This initializes the connection and starts the need threads.
     * @param socket is a {@link Socket}
     * @param socketServer is a {@link SocketServer}*/
    public ClientHandlerSocket(Socket socket, SocketServer socketServer) {
        this.socketServer = socketServer;
        this.socketClient = socket;

        this.lock = new Object();
        this.inputLock = new Object();
        this.outputLock = new Object();
        this.upLock = new Object();

        try{
            this.output = new ObjectOutputStream(socketClient.getOutputStream());
            this.input = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        threadAskDisconnection = new Thread(()->{
            askServerDisconnection();
        },"AskDisconnection");
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
        threadCheckUpdates = new Thread(()-> {
            synchronized (upLock) {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        askControllerBoardUpdate();
                    } catch (IOException | ClassNotFoundException | InterruptedException e) {
                        //throw new RuntimeException(e);
                    }
                }
            }
        },"CheckUpdateBoard");
    }

    /**
     * Method used to get updated from the board.
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found
     * @throws InterruptedException if a thread gets interrupted  */
    private void askControllerBoardUpdate() throws IOException, ClassNotFoundException, InterruptedException {
        do {
            Event e = (Event) socketServer.receivedMessage(new SocketMessage(clientIndex, matchIndex, clientIndex, SET_UP_BOARD));
            ///TODO Event e2 = server.sendMessage(this.matchIndex, myIndex, UPDATE_SCORINGTOKEN);
            if (e == SET_UP_BOARD) {
                Board board = (Board) socketServer.receivedMessage(new SocketMessage(clientIndex, matchIndex, ASK_MODEL, GAME_BOARD));
                sendMessage(new SocketMessage(clientIndex, matchIndex, board, UPDATED_GAME_BOARD));
            }
        } while(!stoppati);
        upLock.wait();
    }

    /**
     * Method used to ask the server if there has been a disconnection.*/
    private void askServerDisconnection() {
        boolean serverAns = false;
        do {
            serverAns = socketServer.askDisconnection(matchIndex);
        }while (!serverAns);
        sendMessage(new SocketMessage(clientIndex,matchIndex,null,Event.DISCONNECTED));
        threadAskDisconnection.interrupt();
        threadAskPit.interrupt();
    }

    /**
     * Method used to get the controller for the player in turn.
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found
     * @throws InterruptedException if a thread gets interrupted  */
    private void askPitController() throws IOException, ClassNotFoundException, InterruptedException {
        stoppati=false;
        do {
            pit = socketServer.askPit(matchIndex);
            if(first && pit!=clientIndex){
                first=false;
                System.out.println("pit diverso" + pit + "cl " + clientIndex);
                sendMessage(new SocketMessage(clientIndex, matchIndex, pit, Event.NOT_YOUR_TURN));
            }
            Thread.sleep(1000);
        } while(pit !=clientIndex);
        stoppati=true;
        first = true;
        System.out.println("manda messaggio è il tuo turno! " + clientIndex);
        sendMessage(new SocketMessage(clientIndex, matchIndex, pit, Event.START_YOUR_TURN));
        lock.wait();
    }

    /**
     * Method used to get input from a client and sent it to another client*/
    @Override
    public void run() {
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
                        if(!threadCheckUpdates.isAlive())
                            threadCheckUpdates.start();
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
                        synchronized (upLock) {
                            this.upLock.notifyAll();
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
            disconnect();
        }
        /*try {
            socketClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    /**
     * Method used to handle the disconnection.*/
    public void disconnect() {
        try{
            if(!socketClient.isClosed()){
                socketClient.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        Thread.currentThread().interrupt();
        socketServer.onDisconnect(matchIndex);
    }

    //SWITCH ERRORI DA SERVER/CONTROLLER

    /**
     * Method used to perform updates on a specific object based on the message received.
     * @param obj is the {@code Object} on which the update will be performed
     * @param message is the {@link SocketMessage} that is used to decide the type of action
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found
     * */
    public void update(Object obj, @NotNull SocketMessage message) throws IOException, ClassNotFoundException {
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
                if(!threadAskDisconnection.isAlive())
                    threadAskDisconnection.start();
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


    /**
     * Method used to send messages to the client.
     * @param message is a {@link SocketMessage}*/
    public void sendMessage(SocketMessage message){
        try {
            output.writeObject(message);
            output.flush();
            output.reset();
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
    }

    /**
     * Method used to receive messages from the client.
     * @return a {@link SocketMessage}
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found */
    public SocketMessage receivedMessage() throws IOException, ClassNotFoundException {
        Object o = input.readObject();
        SocketMessage message = (SocketMessage) o ;
        return message;
    }

    /**
     * Method used to handle the disconnection.
     * @throws RemoteException if an error occurs during a remote call*/
    @Override
    public void ping() throws RemoteException {
    }

    /**
     * Method used to ask the first client to connect (for a match) the number of players accepted for a match.
     * @return an int that represents the number of players for a match
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found*/
    @Override
    public int askNumOfPlayers() throws IOException, ClassNotFoundException {
        sendMessage(new SocketMessage(clientIndex, matchIndex, numOfPlayers, Event.ASK_NUM_PLAYERS));
        return (int)receivedMessage().getObj();
    }
}
