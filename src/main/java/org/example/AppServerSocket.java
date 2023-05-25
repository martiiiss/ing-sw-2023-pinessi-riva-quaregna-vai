package org.example;

import distributed.Socket.SocketServer;

import java.io.IOException;

public class AppServerSocket {

    public static void main(String[] args) throws IOException {
        int portSocket = 43808;
        SocketServer socketServer = new SocketServer(portSocket);
        Thread thread = new Thread(socketServer, "server");
        thread.start();
    }
}
