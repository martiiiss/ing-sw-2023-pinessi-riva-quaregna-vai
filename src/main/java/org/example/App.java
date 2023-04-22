package org.example;

import controller.Controller;
import view.UserInterface;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );
        UserInterface UI = new UserInterface();
        //Here I will put the main functions that wil get called if we
        //decide that what I've done is ok.
        //The next things I'll do are ONLY to see if I had a good idea
        Controller c = new Controller();

        c.createGame();
        c.userChoices();//first player
        c.userChoices();//second player
        c.initializeGame();
        c.gameFlow();
    }
}

