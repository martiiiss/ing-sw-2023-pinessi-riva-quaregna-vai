package org.example;

import distributed.RMI.RMIServer;
import distributed.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppServerRMI {
    public static void main(String[] args) throws RemoteException {
        int port = 43801;
        Server server = new Server(port);
        RMIServer serverRmi = new RMIServer(server, port);
        serverRmi.startServer(serverRmi);
        System.out.println("ciao");
    }


}
