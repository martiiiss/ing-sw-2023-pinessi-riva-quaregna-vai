package distributed.RMI;

import distributed.Connection;
import distributed.Server;

public class RMIConnection extends Connection {
    private final Server server;
    private final ClientConnectionRMI clientConnection;

    public RMIConnection(Server server, ClientConnectionRMI ccRMI){
        this.server = server;
        this.clientConnection = ccRMI;
    }
    @Override
    public void disconnected() {

    }
    @Override
    public void ping() {

    }
}
