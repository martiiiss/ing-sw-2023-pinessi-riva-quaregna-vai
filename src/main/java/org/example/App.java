package org.example;

import controller.Controller;
import distributed.Server;
import view.UserInterface;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    static UserInterface UI;
    static Controller c;

    public static void main(String[] args) throws IOException {
        //Here I will put the main functions that wil get called if we
        //decide that what I've done is ok.
        //The next things I'll do are ONLY to see if I had a good idea
        c = new Controller();
        //UI = c.getInstanceOfUI();



        c.createGame();
     //   c.userChoices();//first player
     //   c.userChoices();//second player
        c.initializeGame();
    }

    public UserInterface getInstanceOfUserInterface(){
        return UI;
    }

    public Controller getInstanceOfController(){
        return c;
    }
}

