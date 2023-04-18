package distributed;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImplementation extends UnicastRemoteObject implements Client, Runnable {

    public ClientImplementation() throws RemoteException {
        super();

    }

    @Override
    public void run() {

    }
}
