package model;

import jdk.jshell.spi.ExecutionControl;


import java.util.ArrayList;

import static model.Type.NOTHING;

public class Player {
    private String nickname;
    private PersonalGoalCard myGoalCard;
    private boolean isFirstPlayer;
    private int score = 0;
    private Bookshelf myBookshelf;
    private ArrayList<Tile> tilesInHand; //This attribute saves the tiles selected by the player in chooseNTiles so that the bookshelf can be filled with fillBookshelf
    private int completedPGC = 0;  //tiles of PGC in correct position
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

    public String getNickname(){
        return this.nickname;
    }

    /*Assign to each player their bookshelf*/
    public void setMyBookshelf() {
        this.myBookshelf = new Bookshelf();
    }

    /*Returns the player's bookshelf*/
    public Bookshelf getMyBookshelf() {
        return this.myBookshelf;
    }

    /*Returns the player's current score*/
    public int getScore(){
        return this.score;
    }

    /*Updates the score: add addScore*/
    public void updateScore (int addScore) {
        this.score += addScore;
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
    public void setScoringToken1(boolean st1){
        this.scoringToken1 = st1;
    }

    public boolean getScoringToken2(){ return this.scoringToken2; }

    public void setScoringToken2(boolean st2){
        this.scoringToken2 = st2;
    }

    /*TilesInHand correspond to the tiles that the player wishes to add to their bookshelf*/
    public ArrayList<Tile> getTilesInHand(){
        return this.tilesInHand;
    }

    public void setTilesInHand(ArrayList<Tile> chosenTiles){
        this.tilesInHand = chosenTiles;
    }

    public int checkCommonGoalCard(CommonGoalCard cgc){
        // se il flag della commonGoalCard (cgc: 1 o 2) è false, verifica se la common goal card cgc è soddisfatta invocando compare di CGC
        //  se lo è setta il flag corrispondente e ritorna il punteggio da aggiungere
        return 0;
        //TODO implement this method
    }

    public int checkCompletePGC(){
        //verifica la personal con le nuove carte aggiunte in bookshelf e ritorna il punteggio da aggiungere.
        return 0;
        //TODO implement this method
    }

    public int checkAdjacentBookshelf(){
        // controlla e ritorno il punteggio delle tessere adiacenti (punteggio da aggiungere)
        return 0;
        //TODO implement this method
    }

    public boolean checkBookshelf(){
        for(int i=0; i<5; i++) {
            if (myBookshelf.getBookshelf()[0][i].getType() == Type.NOTHING) {
                return false;
            }
        }
        myBookshelf.setAsFull();
        return true;  //return true if myBookshelf is full
    }

    public ArrayList<String> readChat(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}

        return null;
        //TODO implement this method
    }

    public void writeChat(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}

        //TODO implement this method
    }
}


