package distributed.RMI;

import distributed.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer extends Server implements ServerRMIInterface {
    private final Server server;
    private final int port;
    private long serialVersionUID = -8672468904670634209L;


    public RMIServer(Server server, int port) throws RemoteException {
        super(port, 2);
        this.server = server;
        this.port = port;
    }

    public void startServer(){
        try{
            Registry registry = LocateRegistry.createRegistry(this.port);
            registry.rebind("server", this);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void login(String username, ClientConnectionRMI clientConnection) throws RemoteException{

        //TODO
    }

    public void disconnect(){
        //TODO
    }
}
