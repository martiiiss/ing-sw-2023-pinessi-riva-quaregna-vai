package view;
import controller.*;

import java.util.Scanner;

public class UserInterface {
    Controller cont;

    public void run(){
        cont = new Controller();
        cont.createGame();
    } //this is probably wrong
    Scanner sc = new Scanner(System.in);

    //Boh, added this one just because
    public void greetings(){
        System.out.print("""
                Hi Welcome to MyShelfie!
                Do you want to read the rules?
                Press 1 for 'Yes', 0 for 'No'
                """);
        int ruleChoice = sc.nextInt();
        if (ruleChoice==1){
            //Here will be invoked a magic function that will show the rules :)
        } else if(ruleChoice==0){
            System.out.println("\nOk, let's go!");
        } else {
            while (ruleChoice!=0 || ruleChoice!=1) {
                System.out.println("Sorry, I don't understand, try again...");
                ruleChoice = sc.nextInt();
            }
        }
    }

    public int askNumOfPlayers(){
        System.out.print("How many players do you want to play with?\n Insert a number between 2 and 4");
        return sc.nextInt();
    }//TODO implement this method

    public String askPlayerNickname(){
        System.out.print("\nChoose a nickname:");
        return sc.nextLine();
    }//TODO implement this method

    public int webProtocol(){
        System.out.print("\nChoose a comunication protocol, \ndigit 1 for 'Socket', 2 for 'JavaRMI':");
        return sc.nextInt();
    }

    public int userInterface(){
        System.out.print("\nDo you prefer a Terminal User Interface (TUI) or a Graphical User Interface (GUI)?\n" +
                            "Press 1 for 'TUI', 2 for 'GUI':");
        return sc.nextInt();
    }//TODO implement this method

    public int askPlayAgain(){
        System.out.print("Dou you want to play again?\nPress 0 for 'No', 1 for 'Yes':");
        return sc.nextInt();
    }
}
