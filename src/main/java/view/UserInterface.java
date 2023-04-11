package view;
import controller.*;
public class UserInterface {
    Controller cont;
    public void run(){
        cont = new Controller();
        cont.createGame();
    }
    public int askNumOfPlayers(){
        int numberOfPlayers=2;
        return numberOfPlayers;
    }//TODO implement this method

    public String askPlayerNickname(){
        String nickname = "nickname";
        return nickname;
    }//TODO implement this method

    public String webProtocol(){
        String chosenProtocol = "protocol";
        return chosenProtocol;
    }

    public String userInterface(){
        String userInterface = "UI";
        return userInterface;
    }//TODO implement this method
}
