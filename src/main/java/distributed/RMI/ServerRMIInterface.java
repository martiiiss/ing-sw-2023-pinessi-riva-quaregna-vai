package distributed.RMI;


import distributed.ClientInterface;
import util.Event;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**Class that represents the interface for an RMI Server.
 * This class calls methods from {@link controller.Controller Controller} in order to manage the game flow.*/
public interface ServerRMIInterface extends Remote {

    /**
     * Method that starts a specific Server (given in input as a parameter).<br>
     * @param serverRmi is a specific {@link RMIServer} that has to be started
     * @throws RemoteException if an error occurred during a remote call*/
    void startServer(ServerRMIInterface serverRmi) throws RemoteException;

    /**
     * Method used to initialize a specific Client (given in input as a parameter).<br>
     * @param rmiClient is a specific {@link RMIClient} that has to be initialized
     * @return an {@code ArrayList} of {@code Integer} that represents the clients
     * @throws IOException if an error occurs
     * @throws AlreadyBoundException if a client has already been initialized
     * @throws ClassNotFoundException if a class cannot be loaded
     * */
    ArrayList<Integer> initClient(ClientInterface rmiClient) throws IOException, AlreadyBoundException, ClassNotFoundException;

    /**
     * Method used to send messages from the Server to the Clients.<br>
     * @param gameIndex an int that represents the index for a match (More that a single match)
     * @param obj a generic object that can be passed to the client to update its view (can be null)
     * @param event a specific {@code Event} used together with the object
     * @return an {@link Event} used to check
     * @throws IOException if an error occurs*/
    Event sendMessage(int gameIndex, Object obj, Event event) throws IOException;

    /**
     * Method used to get info from the model classes.<br>
     * @param gameIndex an int that represents the index for a match (More that a single match)
     * @param event a specific {@code Event} that represents an action to be performed
     * @param clientIndex the index of a specific client
     * @return an {@code Object} that is needed to perform some action
     * @throws RemoteException if an error occurs during a remote call*/
    Object getModel(int gameIndex, Event event, Object clientIndex) throws RemoteException;

    /**
     * Method used to manage the disconnections.<br>
     * @param matchIndex an int that represents the index for a match (More that a single match)
     * @return a boolean, if a clients disconnects <b>true</b>, <b>false</b> otherwise
     * @throws RemoteException if an error occurs during a remote call*/
    boolean getDisconnection(int matchIndex) throws RemoteException;
}
