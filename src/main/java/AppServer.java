import distributed.RMI.RMIServer;
import distributed.RMI.ServerRMIInterface;
import distributed.Server;
import distributed.Socket.SocketServer;
import java.io.IOException;

public class AppServer {
    public static void main(String[] args) throws IOException {
        int portSocket = 43808;
        int portRMI = 45398;
        Server server= new Server(portSocket, portRMI);
        //server RMI
        //String add = "192.168.1.68";
        String add = "localhost";
        System.setProperty("java.rmi.server.hostname", add);
        ServerRMIInterface serverRMI = new RMIServer(portRMI, server);
        serverRMI.startServer(serverRMI);

        //server socket
        SocketServer socketServer = new SocketServer(portSocket, server);
        Thread thread = new Thread(socketServer, "serverSocket");
        thread.start();

    }

}
