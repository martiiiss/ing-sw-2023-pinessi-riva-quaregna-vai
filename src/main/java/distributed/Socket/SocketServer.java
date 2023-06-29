package distributed.Socket;


import distributed.Server;
import distributed.messages.SocketMessage;
import org.jetbrains.annotations.NotNull;
import util.Event;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**Class that represents the Socket server*/
public class SocketServer extends UnicastRemoteObject implements Runnable{
    private final Server server;
    private final int port;
    private static final List<ClientHandlerSocket> clients = new ArrayList<>();


    /**
     * Constructor of the Class. This initializes the port and the server for the socket server.
     * @param server is a {@code Server} that has to be started
     * @param port is the number of port for the server */
    public SocketServer(int port, Server server) throws IOException {
        this.server = server;
        this.port = port;
    }

    /**Method that waits for the connection of a {@code Client}.
     * This method starts the server and waits the connection of a Client.*/
    @Override
    public void run()  {
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            return;
        }
        System.out.println("ServerSocket ready on port: "+port);
        while(!Thread.currentThread().isInterrupted()){
            try{
                Socket socketClient = serverSocket.accept(); //client socket
                socketClient.setSoTimeout(0); //inf
                ClientHandlerSocket clientHandlerSocket = new ClientHandlerSocket(socketClient, this);
                clients.add(clientHandlerSocket);
                Thread clientThread = new Thread(clientHandlerSocket);
                clientThread.start();
                System.out.println("A new client connected!");
            } catch (IOException e){
            }
        }
    }

    /**
     * Method that gets the index of a player.
     * @return an {@code Object}
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found*/
    public synchronized Object askMyIndex () throws IOException, ClassNotFoundException {
        return server.connection(clients.get(clients.size()-1));
    }

    /**
     * Method used to perform a specific action based on a received message.
     * @param message is a {@link SocketMessage} that contains some information
     * @return an {@code Object} that is the object that has to be used
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found */
    public Object receivedMessage(@NotNull SocketMessage message) throws IOException, ClassNotFoundException {
        Object obj;
        if(message.getObj()==Event.CHECK_MY_TURN){
            return server.getServerModel(message.getMatchIndex(), message.getMessageEvent(), message.getClientIndex());
        } else{
            if(message.getObj()==Event.ASK_MODEL){
                obj = server.getServerModel(message.getMatchIndex(), message.getMessageEvent(), message.getClientIndex());
            } else if(message.getMessageEvent()==Event.ASK_NUM_PLAYERS){
                obj = askMyIndex();
            } else {
                obj = server.sendServerMessage(message.getMatchIndex(), message.getObj(), message.getMessageEvent());
            }
            return obj;
        }
    }

    /**
     * Method used to get the player in turn for a match.
     * @param matchIndex an int that represents the index of a match
     * @return an int that represents the index of the player in turn*/
    public int askPit(int matchIndex) {
        return server.askPitController(matchIndex);
    }


    /**
     * Method used to handle the disconnections.
     * @param matchIndex an int that represents the index of a match */
    public void onDisconnect(int matchIndex) {
        server.onDisconnect(matchIndex);
    }

    /**
     * Method used to handle the disconnections.
     * @param matchIndex an int that represents the index of a match
     * @return a boolean that flags a disconnection*/
    public boolean askDisconnection(int matchIndex) {
        return server.askMatchDisconnection(matchIndex);
    }
}

