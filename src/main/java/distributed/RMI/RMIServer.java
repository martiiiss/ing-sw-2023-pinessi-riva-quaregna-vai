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


    public void initPlayer() throws IOException {
        System.out.println("sono in Server");

        server.getInstanceOfController().chooseNickname(this);
    }
    public RMIServer(Server server, int port) throws IOException {
        super(port, 2);
        this.server = server;
        this.port = port;
        clients = new ArrayList<>();
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

    @Override
   /* public void login(String username, ClientConnectionRMI clientConnection) throws RemoteException {
        RMIConnection rmiConnection = new RMIConnection(server, clientConnection);
        server.login(username, (Connection) clientConnection);
    }*/

    public void disconnect(){
        //TODO
    }

    @Override
    public void initClient(Client rmiClient) throws IOException {
        server.connection(rmiClient);
    }

    public boolean getNumberOfPlayer(int num) throws IOException {
        if(server.setClientNumber(num)){
            System.out.println("Number of player expected is: " + num);
            return true;
        } else {
            System.err.println("Number of player wrong!");
            return false;
        }
    }
    public int getNumberOfConnections() {
        return Integer.parseInt(server.getClientsConnected());
    }
    public void registerClient(Callback client) throws RemoteException {
        clients.add(client);
        System.out.println("Client added successfully with callback...");
    }
    public void onEventInserted(Object obj) throws IOException {
        //System.out.println("Nickname passato "+obj.toString());
        super.getUpdates(obj);
    }

}
