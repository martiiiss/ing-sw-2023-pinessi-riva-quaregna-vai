import distributed.RMI.RMIServer;
import distributed.RMI.ServerRMIInterface;
import distributed.Server;
import distributed.Socket.SocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppServer {
    public static void main(String[] args) throws IOException {
        int portSocket = 43808;
        int portRMI = 45398;
        Server server= new Server();

        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        String add;
        System.out.println("Insert address:");
        add = (reader.readLine());

        //server RMI
        //String add = "localhost";
        System.setProperty("java.rmi.server.hostname", add);
        ServerRMIInterface serverRMI = new RMIServer(portRMI, server);
        serverRMI.startServer(serverRMI);

        //server socket
        SocketServer socketServer = new SocketServer(portSocket, server);
        Thread thread = new Thread(socketServer, "serverSocket");
        thread.start();

    }

}
