package org.example;

import distributed.Socket.ClientSocket;
import distributed.Socket.SocketServer;

import java.io.IOException;

public class AppClientSocket {
    public static void main(String[] args){
        String address = "localhost";
        int portSocket = 43808;
        ClientSocket clientSocket = new ClientSocket(address, portSocket);

        try{
            clientSocket.startConnection();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
