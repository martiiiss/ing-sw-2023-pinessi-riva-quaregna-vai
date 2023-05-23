package distributed.Socket;

import distributed.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketServer extends Server implements Runnable{

    private final Server server;
    private final int port;
    private ServerSocket serverSocket = null;
    //BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);



    public SocketServer(Server server, int port) throws IOException {
        super(port, -1);
        this.server = server;
        this.port = port;
        //TODO
    }

    public void startServer(Server server) throws IOException {
        serverSocket = new ServerSocket(port);
       // Socket socket = serverSocket.accept();
        //TODO
    }
    @Override
    public void run() {
        //TODO
    }
}
