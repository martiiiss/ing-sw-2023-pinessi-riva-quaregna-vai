package model;

import jdk.jshell.spi.ExecutionControl;


import java.util.ArrayList;

public class Player {

    private String nickname;
    private PersonalGoalCard myGoalCard;
    private boolean isFirstPlayer;
    private int score = 0;
    private Bookshelf myBookshelf;
    private ArrayList<Tile> tilesInHand; //This attribute saves the tiles selected by the player in chooseNTiles so that the bookshelf can be filled with fillBookshelf
    private int completedPGC = 0;  //tiles of PGC in correct position
    private Player nextPlayer;
    private boolean scoringToken1 = false;
    private boolean scoringToken2 = false;

    // the last two attributes are used to control if the Player has already completed the CGC



    /*This method will be launched when the game starts once game chooses the first player. it will update isFirstPlayer*/
    public void setAsFirstPlayer() {
        this.isFirstPlayer = true;
    }

    /*Sets the player's nickname this method will be invoked by the view and will pass the arg "nickname"*/
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /*Each player knows which player comes after them.*/
    public void setNextPlayer(Player next){
        this.nextPlayer = next;
    }
    /*Whenever a player finishes their turn, game will update "playerInTurn" using this method*/
    public Player getNextPlayer(){
        return nextPlayer;
    }

    /*Assign to each player their bookshelf*/
    public void setMyBookshelf(Bookshelf bks) {
        this.myBookshelf = bks;
    }

    /*Returns the player's bookshelf*/
    public Bookshelf getMyBookshelf() {
        return this.myBookshelf;
    }

    /*Returns the player's current score*/
    public int getScore(){
        return this.score;
    }

    /*Updates the score*/
    public void updateScore (int newScore) {
        this.score = newScore;
    }

    /*Each player gets assigned a personal goal card that they have to complete*/
    public void setPersonalGoalCard(PersonalGoalCard pgc) {
        this.myGoalCard = pgc;
    }

    /*Returns the player's personal goal card*/
    public PersonalGoalCard getPersonalGoalCard() {
        return this.myGoalCard;
    }
    /*completedPGC keeps track of "how much of the personal goal card" has been completed by the player,
    /*or how many tiles of the bookshelf are in the "right position" */
    public void setCompletePGC(int updatedPGC){
        this.completedPGC = updatedPGC;
    }

    public int getCompletePGC(){
        return this.completedPGC;
    }

    public boolean getScoringToken1(){
        return this.scoringToken1;
    }

    /*Once the player completes a CommonGoalCard, they won't be able to collect other points from that CGC.
    /*This method updates the flag that keeps track of whether the player has already collected points from the first card or not*/
    public void setScoringToken1(){
        this.scoringToken1 = true;
    }

    public boolean getScoringToken2(){
        return this.scoringToken2;
    }

    public void setScoringToken2(){
        this.scoringToken2 = true;
    }

    /*TilesInHand correspond to the tiles that the player wishes to add to their bookshelf*/
    public ArrayList<Tile> getTilesInHand(){
        return this.tilesInHand;
    }

    public void setTilesInHand(ArrayList<Tile> chosenTiles){
        this.tilesInHand = chosenTiles;
    }
}


