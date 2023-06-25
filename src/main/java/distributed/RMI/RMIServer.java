package distributed.RMI;

import distributed.Client;
import distributed.Server;
import model.Board;
import util.Event;
import util.Event;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer  extends UnicastRemoteObject implements ServerRMIInterface {
    private final Server server;
    private final int port;
    private RMIClient ConnectedClient;
    private long serialVersionUID = -8672468904670634209L;
    

    public RMIServer(int port, Server server) throws IOException {
        this.server = server;
        this.port = port;
    }

    private Registry registry;
    public void startServer(ServerRMIInterface stub) throws RemoteException{
        try{
            registry = LocateRegistry.createRegistry(this.port);
            registry.rebind("server", stub); //this è l'oggetto remoto
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("Failed to bind to RMI registry");
        }
    }

    public void disconnect(){
        //TODO
    }

    private int index = 0;
    public ArrayList<Integer> initClient(ClientInterface rmiClient) throws IOException, ClassNotFoundException {
        ClientInterface clientInterface = (ClientInterface) rmiClient;
        System.out.println(registry.list());
        return server.connection(clientInterface);
    }

    public Event sendMessage(int gameIndex, Object obj, Event event) throws IOException {
        return server.sendServerMessage(gameIndex, obj,event);
        //return getInstanceOfController().getNextEvent(super.getNumberOfClientsConnected());
    }
    public Object getModel(int gameIndex, Event event, Object clientIndex) throws RemoteException{
        return server.getServerModel(gameIndex, event, clientIndex);
    }
    public boolean getDisconnection(int matchIndex) throws RemoteException{
        return server.getDisconnections(matchIndex);
    }
}
