package org.example;

import distributed.RMI.RMIServer;
import distributed.RMI.ServerRMIInterface;
import distributed.Server;
import distributed.Socket.SocketServer;

import java.io.IOException;
import java.rmi.RemoteException;

import static java.lang.Integer.parseInt;

public class AppServer {
    public static void main(String[] args) throws IOException {
        int portSocket = 43808;
        int portRMI = 45398;
        Server server= new Server(portSocket, portRMI);
        //server RMI
        System.setProperty("java.rmi.server.hostname", "localhost");
        ServerRMIInterface serverRMI = new RMIServer(portRMI, server);
        serverRMI.startServer(serverRMI);

        //server socket
        SocketServer socketServer = new SocketServer(portSocket, server);
        Thread thread = new Thread(socketServer, "serverSocket");
        thread.start();


        /* Server server = new Server(portSocket, portRMI);

        server.startServers(1); //start socket
        SocketServer serverSocket = server.getInstanceOfSocketServer();

        server.startServers(2); // start RMI
        ServerRMIInterface serverRmi = server.getInstanceOfRMIServer();

        System.out.println("Server ready");
*/


    }

}
