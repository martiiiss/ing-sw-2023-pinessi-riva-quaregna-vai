package distributed.Socket;

import distributed.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket extends Client implements Runnable{
    private static final long serialVersionUID = -3886988960361654933L; //random number
    private transient Socket socket;
    private transient ObjectInputStream inputStream;
    private transient ObjectOutputStream outputStream;

    public ClientSocket(String username, String address, int port /*DisconnectionListener disconnectionListener*/){
        super(username,address,port);
    }

    public void startConnection(){
      //TODO implement this
    }

    /*

    @Override
    public void sendMessage(Message message) {
        //TODO implement this
    };*/

    public void closeConnection(){
        //TODO implement this
    };

    public void disconnect(){
        //TODO implement this
    };

    @Override
    public void run() {

    }
}
