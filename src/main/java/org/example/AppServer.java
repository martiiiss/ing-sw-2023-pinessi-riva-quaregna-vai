package org.example;

import controller.Controller;
import distributed.RMI.RMIServer;
import distributed.RMI.ServerRMIInterface;
import distributed.Server;


import java.rmi.RemoteException;

public class AppServer {
    public static void main(String[] args) throws RemoteException {
        int port = 43801;
        Server server = new Server(port, 2); // 2 = RMI
        Controller controller = server.getInstanceOfController();
        ServerRMIInterface serverRmi = new RMIServer(server, port);

        serverRmi.startServer(serverRmi); //oggetto remoto

        System.out.println("Server ready");
    }

}
