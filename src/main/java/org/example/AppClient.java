package org.example;

import controller.ClientController;
import controller.Controller;
import distributed.Client;
import distributed.RMI.ClientInterface;
import distributed.RMI.RMIClient;
import distributed.Socket.ClientSocket;
import model.Game;
import view.UserView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.rmi.NotBoundException;

import static util.Event.END;
import static util.Event.WAIT;

public class AppClient {
    public static void main(String[] args) throws IOException, NotBoundException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        System.out.println("1 for socket, 2 for RMI :");
        int choice = Integer.parseInt(reader.readLine());
        while(choice!=2 && choice!=1){
            System.err.println("Retry (1 Socket, 2 RMI): ");
            choice = Integer.parseInt(reader.readLine());
        }
        if(choice==2) {
            String name = "rmi://localhost:45398/server";
            ClientInterface client = new RMIClient(name, 45398);
            ((RMIClient) client).startConnection();
            System.out.println("You chose RMI!");
            ((RMIClient) client).lobby();

        } else if(choice == 1){
            System.out.println("You chose Socket!");

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
}
