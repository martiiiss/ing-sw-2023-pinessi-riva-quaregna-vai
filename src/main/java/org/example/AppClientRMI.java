package org.example;

import controller.Controller;
import distributed.Client;
import distributed.RMI.RMIClient;
import distributed.RMI.RMIServer;
import distributed.RMI.ServerRMIInterface;
import distributed.Server;
import model.Game;
import view.UserInterface;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class AppClientRMI {
    public static void main(String[] args) throws IOException, NotBoundException {
        Controller controller = new Controller();  //ogni client ha un suo controller
        controller.createGame();
        Game game = controller.getInstanceOfGame();
        UserInterface UI = controller.getInstanceOfUI();

        ServerRMIInterface s = (ServerRMIInterface) Naming.lookup("rmi://localhost:43801/server");
        s.stampa();

        String host = "server";

        RMIClient client = new RMIClient(host,  "", 43801);

        client.startConnection();
        System.out.println("ok");
    }
}
