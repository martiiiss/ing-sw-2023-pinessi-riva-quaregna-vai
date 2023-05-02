package distributed;

import java.rmi.Remote;
import java.util.Timer;

public abstract class Client implements Remote {
    private static final long serialVersionUID = -8499166750855847908L; //random number
    //transient DisconnectionListener disconnectionListener
    private transient Timer pingTimer;
    private String address;
    private String username;
    private int port;
    private String token;

    Client(String username, String address, int port) {
        this.username = username;
        this.address = address;
        this.port = port;
    }
public String getAddress(){

    //TODO implement this

    return null;
}

public int getPort(){
    //TODO implement this

    return 0;
}

public abstract void startConnection();

public void setToken(){this.token = token;}

public String getToken(){return token;}

//public abstract void sendMessage(Message message); //TODO manca la classe Message, va creata

public abstract void closeConnection();

}
