package model;

import java.util.ArrayList;
import java.util.HashMap;



public class Chat {
    private HashMap<Player, ArrayList<String>> playerMessages;

    //The Constructor initializes the HashMap with an empty message so that the method getMessages won't throw exceptions even at the beginning
    public Chat( ArrayList<Player> listOfPlayers) { //listOfPlayers  refers to "player" in Game
        playerMessages = new HashMap<>();
        listOfPlayers.forEach((n)-> {
            ArrayList<String> emptyMsg = new ArrayList<>();
            playerMessages.put(n,emptyMsg);
        });
    }

    public void writePublicMessage(Player sender, String message, ArrayList<Player> listOfPlayers) { /**I really don't like the fact that I have to pass this as an arg */
        listOfPlayers.forEach((n)->{
            ArrayList<String> pMess = new ArrayList<>();
            pMess = playerMessages.get(n);
            pMess.add('\n'+"["+n.getNickname()+"]: ");
            pMess.add(message);
        });
    }
    public void writePrivateMessage(String message, Player sender, Player receiver) {
        ArrayList<String> pMess = new ArrayList<>();
        pMess = this.playerMessages.get(receiver);
        pMess.add('\n'+"{"+sender.getNickname()+"}: ");
        pMess.add(message);
        this.playerMessages.put(receiver,pMess);
    }

    public ArrayList<String> getMessages(Player player) {
        return this.playerMessages.get(player);
    }
}