package distributed.RMI;

import distributed.Server;
import distributed.Connection;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer extends Server implements ServerRMIInterface {
    private final Server server;
    private final int port;
    private long serialVersionUID = -8672468904670634209L;


    public void stampa() throws RemoteException{
        System.out.println("ciaoo!");
    }
    public RMIServer(Server server, int port) throws RemoteException {
        super(port, 2);
        this.server = server;
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

    @Override
    public void login(String username, ClientConnectionRMI clientConnection) throws RemoteException {
        RMIConnection rmiConnection = new RMIConnection(server, clientConnection);
        server.login(username, (Connection) clientConnection);
    }

    public void disconnect(){
        //TODO
    }
}
