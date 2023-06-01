package org.example;

import controller.ClientController;
import distributed.Socket.ClientSocket;
import distributed.Socket.SocketServer;
import view.UserView;

import java.io.IOException;

public class AppClientSocket {
    public static void main(String[] args) throws IOException {
        String address = "localhost";
        int portSocket = 43808;
        //ClientSocket clientSocket = new ClientSocket(address, portSocket);

        UserView tui = new UserView();
        ClientController clientController = new ClientController(tui);
        tui.addObserver(clientController);

        try{
            clientController.initClient(address,portSocket);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }


    }
}
