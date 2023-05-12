package distributed.RMI;

import distributed.Client;
import distributed.Server;
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
    public void initClient(Client rmiClient) throws IOException {
        server.connection(rmiClient);
        //System.out.println("CIAO user "+server.getClientsConnectedList().size());
    }

    public int getNumberOfConnections() {
        return server.getClientsConnected();
    }

    public Error onEventInserted(Object obj, Event event, int numOfPlayerConnected) throws IOException {
        //System.out.println("Nickname passato "+obj.toString());
        return super.getUpdates(obj, event, numOfPlayerConnected);
    }



    public Event sendMessage() throws RemoteException{
        return server.getEvent();
        //return getInstanceOfController().getNextEvent(super.getNumberOfClientsConnected());
    }
}
