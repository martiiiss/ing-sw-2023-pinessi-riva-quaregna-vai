package distributed.RMI;

import distributed.Client;
import distributed.Server;
import util.Event;
import view.UserInterface;
import view.UserView;

import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient extends Client implements ClientConnectionRMI, Serializable {
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

    /**
     *
     */
    @Override
    public void startConnection() throws RemoteException, NotBoundException {
        try {
            server = (ServerRMIInterface) Naming.lookup(getUsername());
            server.initClient(this);
            //   server.login(getUsername(), this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }  //TODO implement this


    //   @Override
    //  public void sendMessage(Message message) {
    //TODO implement this
    // }

    public String getUsername(){ return this.username; }

    public int getPort(){ return this.port; }

    public void getMessage(){
        //TODO implement this
    }

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
            boolean flag = server.getNumberOfPlayer(uView.askNumOfPlayer());
            while (!flag) {
                System.err.println("Retry: ");
                flag = server.getNumberOfPlayer(uView.askNumOfPlayer());
            }
        }
    }
    public void getClientSettings(String nickname, int userInterface) throws IOException {
        server.sendUpdate(this,nickname, Event.SET_NICKNAME);
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


}
