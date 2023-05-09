package distributed.RMI;

import distributed.Client;
import util.Callback;
import util.Event;
import view.UserView;

import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static util.Event.*;

public class RMIClient extends Client implements ClientConnectionRMI, Serializable, Callback {
    @Serial
    private static final long serialVersionUID = -3489512533622391685L; //random number
    private transient ServerRMIInterface server;
    private String address;
    private String username;
    private int port;

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

    /**
     *
     */
    UserView uView = new UserView();
    @Override
    public void firstClientMessages() throws IOException, RemoteException {
        System.out.println("Number of connections: "+server.getNumberOfConnections());
        if(server.getNumberOfConnections() == 1) {
            boolean flag = server.getNumberOfPlayer(uView.askNumOfPlayer(server.getNumberOfConnections()));
            while (!flag) {
                System.err.println("Retry: ");
                flag = server.getNumberOfPlayer(uView.askNumOfPlayer(server.getNumberOfConnections()));
            }
        }
    }

    /*2public void requestClient() throws IOException {
        String nickname = "";
        nickname = uView.askPlayerNickname();
        server.onEventInserted(nickname);
        //server.sendUpdate(this,nickname,Event.SET_NICKNAME);
    }*/

    @Override
    public void disconnected() {

    }
    public void ping(){
        //TODO implement this
    }

    public void disconnect(){
        //TODO implement this
    }
    public void onEventInserted(Object eventObj) throws RemoteException{
        System.out.println("Event completed successfully!");
    }




    public Event receivedMessage() throws IOException{
        return server.sendMessage();
    }

    boolean flag = true;
    public void actionToDo() throws IOException {
        switch (receivedMessage()){
            case ASK_NUM_PLAYERS -> {
                flag = server.onEventInserted(uView.askNumOfPlayer(server.getNumberOfConnections()), ASK_NUM_PLAYERS);
                while(!flag){
                    System.err.println("Retry, num of player: " + flag);
                    flag = server.onEventInserted(uView.askNumOfPlayer(server.getNumberOfConnections()), receivedMessage());
                }
            }
            case SET_NICKNAME -> {
                server.onEventInserted(uView.askPlayerNickname(), SET_NICKNAME);
            }
      /*      case CHOOSE_NETWORK_PROTOCOL -> {
                flag = server.onEventInserted(uView.webProtocol(), CHOOSE_NETWORK_PROTOCOL);
                while(!flag){
                    System.err.println("Retry: ");
                    flag = server.onEventInserted(uView.webProtocol(), receivedMessage());
                }
            } */
        }
    }
}
