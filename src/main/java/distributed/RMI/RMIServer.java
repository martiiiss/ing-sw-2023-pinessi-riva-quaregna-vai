package distributed.RMI;

import distributed.Client;
import distributed.Server;
import model.Board;
import util.Error;
import util.Event;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer extends Server implements ServerRMIInterface {
    private final Server server;
    private final int port;
    private RMIClient ConnectedClient;
    private long serialVersionUID = -8672468904670634209L;
    

    public RMIServer(int port) throws IOException {
        super(-1, port);
        this.server = super.getInstanceOfServer();
        this.port = port;
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

    public void disconnect(){
        //TODO
    }

    @Override
    public int initClient(Client rmiClient) throws IOException {
        return server.connection(rmiClient);
    }

    public int getNumberOfConnections() {
        return server.getClientsConnected();
    }

    public Error sendMessage(Object obj, Event event) throws IOException {
        return server.sendServerMessage(obj,event);
        //return getInstanceOfController().getNextEvent(super.getNumberOfClientsConnected());
    }
    public Object getModel(Event event, int clientIndex) throws RemoteException{
        return server.getServerModel(event, clientIndex);
    }
}
