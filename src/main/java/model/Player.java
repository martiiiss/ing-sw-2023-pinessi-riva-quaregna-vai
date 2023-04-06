package model;

import control.Controller;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

import static model.Type.CAT;
import static model.Type.NOTHING;


public class Player {
    private String nickname;
    private PersonalGoalCard myGoalCard;
    private boolean isFirstPlayer;
    private int score = 0;
    private Bookshelf myBookshelf;
    private ArrayList<Tile> tilesInHand; //This attribute saves the tiles selected by the player in chooseNTiles so that the bookshelf can be filled with fillBookshelf
    private boolean completedPGC = false;  //tiles of PGC in correct position -> maybe useless
    //TODO ask if this attribute is to delete -> modified into a boolean type
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
    //TODO control if this implementation is the best possible

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
    public void setCompletePGC(){
        this.completedPGC = true;
    }
    //TODO -> modified into a boolean type
    public boolean getCompletePGC(){
        return this.completedPGC;
    }
    //TODO -> modified into a boolean type

    public boolean getScoringToken1(){return this.scoringToken1;}


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


    public int checkCompletePGC() {
        /*Tile[][] bks = myBookshelf.getBookshelf();
        int PGCScore = 0;
        int numberOfTilesCompleted = 0;
        int r,c;
        for(r=0;r<6;r++){
            for (c=0;c<5;c++){
                if(myGoalCard.getPGC()[r][c].getType() == myBookshelf.getBookshelf()[r][c].getType()
                        && myGoalCard.getPGC()[r][c].getType() != NOTHING){
                    numberOfTilesCompleted++;
                }
            }
        }
        /**Based on how many tiles are completed I assign the PGCScore
         *This is good because I don't have to check everytime if
         *I already completed that tile

        switch (numberOfTilesCompleted) {
            case 0 -> PGCScore = 0;
            case 1 -> PGCScore = 1;
            case 2 -> PGCScore = 2;
            case 3 -> PGCScore = 4;
            case 4 -> PGCScore = 6;
            case 5 -> PGCScore = 9;
            case 6 -> {PGCScore = 12; setCompletePGC();} //TODO -> is this ok?
            default -> throw new IllegalStateException("Unexpected value:"+ numberOfTilesCompleted);
        }

         */
        return score; //return PGCScore;
    }

    public int checkAdjacentBookshelf(){
        int adjacencyScore = 0;
        // controlla e ritorno il punteggio delle tessere adiacenti (punteggio da aggiungere)
        return adjacencyScore;
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


    /**This two methods refer to the chat, we have to chose if they're ok in Player
     * and if they're ok as they are
     */
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


