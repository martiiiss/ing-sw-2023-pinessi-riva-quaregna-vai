package org.example;

import distributed.RMI.RMIClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;

import static util.Event.END;

public class AppClient2 {
    public static void main(String[] args) throws IOException, NotBoundException {
        //Controller controller = app.getInstanceOfController();  //ogni client ha un suo controller

        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        System.out.println("1 for socket, 2 for RMI :");
        int choice = Integer.parseInt(reader.readLine());
        while(choice!=2 && choice!=1){
            System.err.println("Retry (1 Socket, 2 RMI): ");
            choice = Integer.parseInt(reader.readLine());
        }
        if(choice==2) {
            String name = "rmi://localhost:45398/server";
            RMIClient client = new RMIClient(name, 45398);
            client.startConnection();
            System.out.println("You chose RMI!");

            while(client.getEventClient()!=END){
               // System.err.print("CHIAMATA RECEIVED MESSAGE: ");
                client.receivedMessage();
               // client.actionToDo();
            }

        } else if(choice == 1){
            //clientSocket
            System.out.println("You chose Socket!");
        }
    }
}