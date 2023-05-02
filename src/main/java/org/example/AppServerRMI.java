package org.example;

import controller.Controller;
import distributed.RMI.RMIServer;
import distributed.Server;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppServerRMI {
    static App app = new App();

    public static void main(String[] args) throws RemoteException {
        int port = 43801;
        Server server = new Server(port, 2); // 2 = RMI
        Controller controller = app.getInstanceOfController();
        RMIServer serverRmi = new RMIServer(server, port);
        serverRmi.startServer();
        System.out.println("ciao");
    }


}
