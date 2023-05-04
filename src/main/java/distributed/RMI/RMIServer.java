package distributed.RMI;

import controller.Controller;
import distributed.Client;
import distributed.Server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer extends Server implements ServerRMIInterface {
    private final Server server;
    private final int port;
    private RMIClient ConnectedClient;
    private long serialVersionUID = -8672468904670634209L;


    public void initPlayer() throws IOException {
        System.out.println("sono in Server");

        server.getInstanceOfController().chooseNickname();
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

    public void getNumberOfPlayer(int num) throws RemoteException{
        System.out.println("Number of player expected is: " + num);
        //server.setClientNumber(num);
    }

    public int getNumberOfConnections() {
        return Integer.parseInt(server.getClientsConnected());
    }
}
