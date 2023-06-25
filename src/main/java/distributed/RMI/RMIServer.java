package distributed.RMI;

import distributed.Server;
import util.Event;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**Class that represents an RMI Server*/
public class RMIServer extends UnicastRemoteObject implements ServerRMIInterface {
    private final Server server;
    private final int port;
    private Registry registry;

    /**
     * Constructor that sets the port of the RMI server and also the actual server.<br>
     * @param server is the actual server
     * @param port int that represents the port number of the {@code RMIServer}
     * @throws IOException if an error occurs */
    public RMIServer(int port, Server server) throws IOException {
        this.server = server;
        this.port = port;
    }

    /**@throws RemoteException if an error occurs during a remote call*/
    public void startServer(ServerRMIInterface stub) throws RemoteException{
        try{
            registry = LocateRegistry.createRegistry(this.port);
            registry.rebind("server", stub);
        } catch (Exception e){
            e.printStackTrace();
            System.err.println("Failed to bind to RMI registry");
        }
    }

    /**@throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be loaded*/
    public ArrayList<Integer> initClient(ClientInterface rmiClient) throws IOException, ClassNotFoundException {
        System.out.println(registry.list());
        return server.connection(rmiClient);
    }

    /**@throws IOException if an error occurs*/
    public Event sendMessage(int gameIndex, Object obj, Event event) throws IOException {
        return server.sendServerMessage(gameIndex, obj,event);
    }
    /**@throws RemoteException if an error occurs during a remote call*/
    public Object getModel(int gameIndex, Event event, Object clientIndex) throws RemoteException{
        return server.getServerModel(gameIndex, event, clientIndex);
    }
    /**@throws RemoteException if an error occurs during a remote call*/
    public boolean getDisconnection(int matchIndex) throws RemoteException{
        return server.getDisconnections(matchIndex);
    }
}
