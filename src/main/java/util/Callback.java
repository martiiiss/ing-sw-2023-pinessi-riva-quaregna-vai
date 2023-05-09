package util;

import java.rmi.RemoteException;

public interface Callback {
    void onEventInserted(Object eventObj) throws RemoteException;
}
