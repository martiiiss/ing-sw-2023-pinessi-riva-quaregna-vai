package model;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Player winner;
    private int numberOfPlayers;
    private Player playerInTurn; // we don't know if we'll have to delete this
    private Player finisher;
    private boolean isLastTurn;

    private int numOfPlayers;

    private ArrayList<Player> players;

    private ArrayList<CommonGoalCard> commonGoalCards;


    public Game() { // added the constructor
    }

    public void setCommonGoalCards(){ //choose 2 commonGoalCard
        int n1 = (new Random()).nextInt(12)+1; //random number (1-12)
        int n2 = (new Random()).nextInt(12)+1;
        if(n1==n2){
            n2++;
        }

        CommonGoalCard c1 = new CommonGoalCard(n1, numberOfPlayers, 1);
        CommonGoalCard c2 = new CommonGoalCard(n2, numberOfPlayers, 2);

        commonGoalCards.add(c1); //add c1, c2 to arrayList
        commonGoalCards.add(c2);
    }

    public ArrayList<CommonGoalCard> getCommonGoalCard(){
        return this.commonGoalCards;
    }

    public void setNumOfPlayers(int nop){ //nop is chosen by the player who creates the game
        this.numberOfPlayers = nop;

    }

    public void setPlayerInTurn(Player pit){
        this.playerInTurn = pit;
    }

    public Player getPlayerInTurn(){
        return this.playerInTurn;
    }

    public void setWinner(Player win){
        this.winner = win;
    }

    public Player getWinner(){
        return this.winner;
    }


    public void setFinisher(Player finished){
        this.finisher = finished; //set finisher
        isLastTurn = true; // it updates the value of isLastTurn to TRUE
    }

    public Player getFinisher(){
        return this.finisher;
    }

    public boolean getIsLastTurn(){
        return this.isLastTurn;
    }

    public void assignPersonalGoalCard(int nOfPlayers){
        int i;
        int idOfPersonalGoalCard;
        for (i=0;i<nOfPlayers;i++) { // iterating through the array of players
             // We have to choose a random number (the pgc) and assign it  to the player
            idOfPersonalGoalCard = (new Random()).nextInt(12);
            //TODO implement this code referring to Player and PersonalGoalCard

            /*  After choosing a PCG we should set it with a method in player
                We need a method that sets the PGC in Player, with that we can invoke that method inside
                assignPersonalGoalCard and set the random number generated as the PGC
            */
        }
        /** I don't know if what I've done here is right **/
    }

    public void addPlayer(Player p){
        players.add(p); // it adds the player into the array
    }

}
