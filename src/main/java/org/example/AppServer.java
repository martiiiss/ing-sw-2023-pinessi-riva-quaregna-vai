package org.example;

import controller.Controller;
import distributed.RMI.RMIServer;
import distributed.RMI.ServerRMIInterface;
import distributed.Server;
import distributed.Socket.SocketServer;


import java.io.IOException;

public class AppServer {
    public static void main(String[] args) throws IOException {
        int port = 43801;

        Server server = new Server(port, port+1); // 2 = RMI
        Controller controller = server.getInstanceOfController();

        server.startServers(1);
        SocketServer serverSocket = server.getInstanceOfSocketServer();
        ServerRMIInterface serverRmi = server.getInstanceOfRMIServer();

        System.out.println("Server ready");
    }

}
