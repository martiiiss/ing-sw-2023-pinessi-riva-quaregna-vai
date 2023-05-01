package distributed;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends Client implements ClientConnectionRMI {

    private static final long serialVersionUID = -3489512533622391685L; //random number
    private transient ServerRMIInterface server;
    private String address;
    private String username;
    private int port;

    public RMIClient(String username, String address, int port) throws RemoteException {
        super(username, address, port);
    }

    /**
     *
     */
    @Override
    public void startConnection() {
        //TODO implement this
    }

    /**
     * @param message
     */
    @Override
    public void sendMessage(Message message) {
        //TODO implement this
    }

    public void getMessage(){
        //TODO implement this
    }

    /**
     *
     */
    @Override
    public void closeConnection() {
        //TODO implement this
    }

    /**
     *
     */
    @Override
    public void messageReceived() {
        //TODO implement this
    }

    public void ping(){
        //TODO implement this
    }

    public void disconnect(){
        //TODO implement this
    }


}
