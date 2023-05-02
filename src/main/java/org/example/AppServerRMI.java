package org.example;

import distributed.RMI.RMIServer;
import distributed.Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppServerRMI {
    public static void main(String[] args){
        try{
            Registry registry = LocateRegistry.createRegistry(43800);
            registry.rebind("server", new RMIServer(new Server(43800), 43800));

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
