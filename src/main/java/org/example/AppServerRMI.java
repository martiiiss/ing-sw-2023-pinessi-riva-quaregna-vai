package org.example;

import controller.Controller;
import distributed.RMI.RMIServer;
import distributed.RMI.ServerRMIInterface;
import distributed.Server;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppServerRMI {
    static App app = new App();

    public static void main(String[] args) throws RemoteException {
        int port = 43801;
        Server server = new Server(port, 2); // 2 = RMI
        Controller controller = app.getInstanceOfController();
        ServerRMIInterface serverRmi = new RMIServer(server, port);

        serverRmi.startServer(serverRmi); //oggetto remoto

        System.out.println("Server ready");
    }


}
