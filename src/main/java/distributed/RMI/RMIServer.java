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
import java.util.List;

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

    private Registry registry;
    public void startServer(ServerRMIInterface stub) throws RemoteException{
        try{
            registry = LocateRegistry.createRegistry(this.port);
            registry.rebind("server", stub); //this è l'oggetto remoto
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("Failed to bind to RMI registry");
        }
        Thread disconnection = new Thread(()-> {
            rmiconnectionManager();
        }, "Thread disconnection handler");
        disconnection.start();
    }

    public void disconnect(){
        //TODO
    }

    private int index = 0;
    public int initClient(ClientInterface rmiClient) throws IOException, AlreadyBoundException {
        ClientInterface clientInterface = (ClientInterface) rmiClient;
        registry.bind("Client"+index,clientInterface);
        index++;
        System.out.println(registry.list());
        return server.connection(clientInterface);
    }

    public int getNumberOfConnections() {
        return server.getClientsConnected();
    }

    public Event sendMessage(Object obj, Event event) throws IOException {
        return server.sendServerMessage(obj,event);
        //return getInstanceOfController().getNextEvent(super.getNumberOfClientsConnected());
    }
    public Object getModel(Event event, Object clientIndex) throws RemoteException{
        return server.getServerModel(event, clientIndex);
    }
    private void rmiconnectionManager() {
        List<ClientInterface> clientInterfaces = server.getClients();
        for (ClientInterface client : clientInterfaces) {
            try {
                client.ping();
            } catch (RemoteException exception) {
                System.out.println("Client disconnesso");
            }
        }
    }

    public boolean getDisconnection() throws RemoteException{
        return server.getDisconnections();
    }
}
