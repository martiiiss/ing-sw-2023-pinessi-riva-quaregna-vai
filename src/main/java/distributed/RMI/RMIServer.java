package distributed.RMI;

import distributed.Client;
import distributed.Server;
import util.Callback;
import util.Event;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class RMIServer extends Server implements ServerRMIInterface {
    private final Server server;
    private ArrayList<Callback> clients;
    private final int port;
    private RMIClient ConnectedClient;
    private long serialVersionUID = -8672468904670634209L;


    /*
    public void initPlayer() throws IOException {
        System.out.println("sono in Server");

        server.getInstanceOfController().chooseNickname(this);
    }
    */

    public RMIServer(int port) throws IOException {
        super(-1, port);
        this.server = super.getIstanceOfServer();
        this.port = port;
        this.clients = new ArrayList<>();
    }

    public void startServer(ServerRMIInterface stub) throws RemoteException{
        try{
            Registry registry = LocateRegistry.createRegistry(this.port);
            registry.rebind("server", stub); //this è l'oggetto remoto
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("Failed to bind to RMI registry");
        }
    }

  /*  @Override
    public void login(String username, ClientConnectionRMI clientConnection) throws RemoteException {
        RMIConnection rmiConnection = new RMIConnection(server, clientConnection);
        server.login(username, (Connection) clientConnection);
    }*/

    public void disconnect(){
        //TODO
    }

    @Override
    public void initClient(Client rmiClient) throws IOException {
        server.connection(rmiClient);
        System.out.println("CIAO user "+server.getClientsConnectedList().size());
    }

    /*
    public boolean getNumberOfPlayer(int num) throws IOException {
        if(server.setClientNumber(num)){
            System.out.println("Number of player expected is: " + num);
            return true;
        } else {
            System.err.println("Number of player wrong!");
            return false;
        }
    }
     */
    public int getNumberOfConnections() {
        return server.getClientsConnected();
    }
    public void registerClient(Callback client) throws RemoteException {
        clients.add(client);

        System.out.println("Client added successfully with callback...");
    }



    public boolean onEventInserted(Object obj, Event event, int numOfPlayerConnected) throws IOException {
        //System.out.println("Nickname passato "+obj.toString());
        return super.getUpdates(obj, event, numOfPlayerConnected);
    }



    public Event sendMessage() throws RemoteException{
        return server.getEvent();
        //return getInstanceOfController().getNextEvent(super.getNumberOfClientsConnected());
    }
}
