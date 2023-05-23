package distributed.Socket;

import distributed.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer extends Server{

    private final Server server;
    private final int port;



    public SocketServer(int port) throws IOException {
        super(port, -1);
        this.server = super.getInstanceOfServer();
        this.port = port;
    }

    public void startServer(int port) throws IOException, ClassNotFoundException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("ServerSocket ready on port: "+port);
        while(true){
            try{
                Socket socket = serverSocket.accept();
                executor.submit(new ClientHandlerSocket((socket)));
            } catch (IOException e){
                break;
            }
        }
        executor.shutdown();
    }
}
