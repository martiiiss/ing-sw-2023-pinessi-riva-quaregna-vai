package org.example;

import controller.Controller;
import distributed.Client;
import distributed.RMI.RMIServer;
import distributed.RMI.ServerRMIInterface;
import distributed.Server;
import distributed.Socket.SocketServer;
import util.Callback;
import util.Event;


import java.io.IOException;
import java.rmi.RemoteException;

import static java.lang.Integer.parseInt;

public class AppServer {
    public static void main(String[] args) throws IOException {
        int portSocket = 43801;
        int portRMI = 45398;
       /* Server server = new Server(portSocket, portRMI);

        server.startServers(1); //start socket
        SocketServer serverSocket = server.getInstanceOfSocketServer();

        server.startServers(2); // start RMI
        ServerRMIInterface serverRmi = server.getInstanceOfRMIServer();

        System.out.println("Server ready");
*/
        ServerRMIInterface serverRMI = new RMIServer(portRMI);
        serverRMI.startServer(serverRMI);

    }

}
