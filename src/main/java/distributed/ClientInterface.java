package distributed;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**Class that represents the interface of a Client*/
public interface ClientInterface extends Remote {
    /**
     * Method used to handle disconnections.<br>
     * This method gets implemented in the specific type of Client.
     * @throws RemoteException when in an RMI type of connection*/
    void ping() throws RemoteException;

    /**
     * Method used to ask the number of players to the first player.<br>
     * This method gets implemented in the specific type of Client.
     * @return an int that represents the number of players for a match
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be loaded*/
    int askNumOfPlayers() throws IOException, ClassNotFoundException;
}
