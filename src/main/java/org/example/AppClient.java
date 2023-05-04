package org.example;

import controller.Controller;
import distributed.Client;
import distributed.RMI.RMIClient;
import model.Game;
import view.UserInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.rmi.NotBoundException;

public class AppClient {
    public static void main(String[] args) throws IOException, NotBoundException {
        //Controller controller = app.getInstanceOfController();  //ogni client ha un suo controller

        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        System.out.println("1 for RMI 2 for socket:");
        int choice = Integer.parseInt(reader.readLine());
        if(choice==1) {
            String name = "rmi://localhost:45398/server";
            RMIClient client = new RMIClient(name, 45398);
            client.startConnection();
            System.out.println("You chose RMI!");

            client.messageReceived();
        } else if(choice == 2){
            //clientSocket
            System.out.println("You chose Socket!");
        }
    }
}