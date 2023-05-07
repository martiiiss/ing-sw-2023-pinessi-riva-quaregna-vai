package distributed;

import controller.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.Buffer;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Timer;
public abstract class Client implements Remote, Serializable {
    private static final long serialVersionUID = -8499166750855847908L; //random number
    //transient DisconnectionListener disconnectionListener
    private transient Timer pingTimer;
    private String address;
    private String username;
    private int port;
    private String token;

    protected Client(String username, int port) {
        this.username = username;
        //   this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }

    public abstract void startConnection() throws RemoteException, NotBoundException;

    public void setToken() {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //public abstract void sendMessage(Message message); //TODO manca la classe Message, va creata

    public abstract void closeConnection();

    public abstract void disconnected();

    public Client getSuper() {
        return this;
    }

    public int askNumberOfPlayers() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        return Integer.parseInt(reader.readLine());
    }
}