/*package distributed.Socket;

import distributed.Connection;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionSocket extends Connection implements Runnable{

    private SocketServer socketServer;
    private Socket socket;
    private boolean connected;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Object outLock;
    private Object inLock;
    private Thread listener;

    void ConnectionSocket(SocketServer socketServer, Socket socket){
        this.socketServer = socketServer;
        this.socket = socket;
        this.connected = true;

        //TODO finire
    }


    @Override
    public boolean isConnected(){
        return connected;
    }

    public void sendMessage(){
        //TODO
    }
    @Override
    public void run() {

    }

    @Override
    public void disconnected() {

    }

    @Override
    public void ping() {

    }
}


 */