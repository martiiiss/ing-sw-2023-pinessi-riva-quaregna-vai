package distributed.RMI;

import distributed.Server;

import java.rmi.RemoteException;

public class RMIServer extends Server implements ServerRMIInterface {
    private final Server server;
    private final int port;
    private long serialVersionUID = -8672468904670634209L;

    public RMIServer(Server server, int port) throws RemoteException {
        super(port);
        this.server = server;
        this.port = port;
    }

    public void startServer(){
        //TODO
    }

    public void login(String username, ClientConnectionRMI clientConnection) throws RemoteException{
        //TODO
    }

    public void disconnect(){
        //TODO
    }
}
