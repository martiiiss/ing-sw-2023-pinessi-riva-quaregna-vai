package model;

import jdk.jshell.spi.ExecutionControl;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException; -> control the type of exception in all code

public class Game {
    private Player winner;
    private int numberOfPlayers;
    private Player playerInTurn;
    private Player finisher;
    private boolean isLastTurn;
    private CommonGoalCard commonGoalCard1;
    private CommonGoalCard commonGoalCard2;
    private Player firstPlayer;


    public void setCommonGoalCards(CommonGoalCard cgc1, CommonGoalCard cgc2 ){
        this.commonGoalCard1 = cgc1;
        this.commonGoalCard2 = cgc2;
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

    public void setFirstPlayer(Player firstPl){
        this.firstPlayer = firstPl; //set firstPlayer
        this.playerInTurn = firstPl; //game start: the firstPlayer is also the playerInTurn
        this.firstPlayer.setAsFirstPlayer();
    }

    public Player getFirstPlayer(){
        return this.firstPlayer;
    }

    public void setFinisher(Player finished){
        this.finisher = finished; //set finisher
        isLastTurn = true; // it updates the value of isLastTurn to TRUE
    }

    public Player getFinisher(){
        return this.finisher;
    }

}
