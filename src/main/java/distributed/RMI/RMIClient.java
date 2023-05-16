package distributed.RMI;

import distributed.Client;
import util.Error;
import util.Event;
import view.UserView;

import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static util.Event.*;

public class RMIClient extends Client implements ClientConnectionRMI, Serializable {
    @Serial
    private static final long serialVersionUID = -3489512533622391685L; //random number
    private transient ServerRMIInterface server;
    private String address;
    private String username;
    private int port;
    private UserView uView = new UserView();


    public RMIClient(String username, int port) throws RemoteException {
        super(username, port);
        this.username = username; //server
        //  this.address = address;
        this.port = port;
    }

    @Override
    public void startConnection() throws RemoteException, NotBoundException {
        try {
            server = (ServerRMIInterface) Naming.lookup(getUsername());
            server.initClient(this);
            // server.registerClient(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }  //TODO implement this


    public String getUsername(){ return this.username; }

    public int getPort(){ return this.port; }

    @Override
    public void closeConnection() {
        //TODO implement this
    }

    @Override
    public void disconnected() {

    }
    public void ping(){
        //TODO implement this
    }

    public void disconnect(){
        //TODO implement this
    }

    private Event eventClient;
    int cont=0;
    public void receivedMessage() throws IOException{
        this.eventClient =  server.sendMessage(myNumOfPlayer);
        while(this.eventClient!=END){
            if(this.eventClient==WAIT && cont==0){
                cont=1;
                actionToDo();
            }else if (this.eventClient!=WAIT){
                cont=0;
                actionToDo();
            }
            this.eventClient = server.sendMessage(this.myNumOfPlayer);
        }
    }

    public Event getEventClient(){
        return eventClient;
    }
    private Error errorReceived;
    int myNumOfPlayer=-1;



    public void actionToDo() throws IOException {
        switch (eventClient){
            case ASK_NUM_PLAYERS -> {
                this.myNumOfPlayer = server.getNumberOfConnections()-1;
                //System.out.println("my numOfPlayer" + this.myNumOfPlayer);
                errorReceived = server.onEventInserted(uView.askNumOfPlayer(server.getNumberOfConnections()), ASK_NUM_PLAYERS, this.myNumOfPlayer);
                if (errorReceived == Error.NOT_AVAILABLE) {
                    System.err.println("Retry, num of player: " + errorReceived);
                }
            }
            case SET_NICKNAME -> {
                this.myNumOfPlayer = server.getNumberOfConnections()-1;
                errorReceived = server.onEventInserted(uView.askPlayerNickname(), SET_NICKNAME, this.myNumOfPlayer);
                if(errorReceived == Error.NOT_AVAILABLE){
                    System.err.println("This nickname is already taken, retry:");
                } else if(errorReceived == Error.EMPTY_NICKNAME){
                    System.err.println("Nickname is empty");
                }
            }
            case CHOOSE_VIEW -> {
                errorReceived = server.onEventInserted(uView.userInterface(), CHOOSE_VIEW, this.myNumOfPlayer);
                if (errorReceived == Error.NOT_AVAILABLE) {
                    System.err.println("Retry: " + errorReceived);
                }
                System.out.println("CHOOSE VIEW");

            }
            case WAIT -> {
                System.out.println("WAIT!!");

                errorReceived = server.onEventInserted( null, WAIT, this.myNumOfPlayer);
                while(errorReceived == Error.WAIT){
                    errorReceived = server.onEventInserted(null, WAIT, this.myNumOfPlayer);
                 //   System.out.println("error:" + errorReceived);
                }

                System.out.println("Errore: " + errorReceived);

            }
            case START -> {
                uView.showTUIBoard(server.getBoard());

                System.out.println("START");


            }
        }
    }
}