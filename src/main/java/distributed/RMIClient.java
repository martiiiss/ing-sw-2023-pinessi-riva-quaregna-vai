package distributed;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends Client implements ClientConnectionRMI {




    public RMIClient() throws RemoteException {
        super();

    }

}
