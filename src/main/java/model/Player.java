package model;

import jdk.jshell.spi.ExecutionControl;
import util.Cord;
import util.Observable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import static model.Type.NOTHING;



public class Player extends Observable implements Serializable {
    private static final long serialVersionUID = 627657924675627426L;
    private String nickname;
    private PersonalGoalCard myGoalCard;
    private boolean isFirstPlayer;
    private int score;
    private Bookshelf myBookshelf;
    private ArrayList<Tile> tilesInHand; //This attribute saves the tiles selected by the player in chooseNTiles so that the bookshelf can be filled with fillBookshelf
    private int scorePGC = 0;  //progressive score of PGC

    private boolean scoringToken1;
    private boolean scoringToken2;
    // the last two attributes are used to control if the Player has already completed the CGC
    private HashMap<ArrayList<Cord>,Integer> previousAdj = new HashMap<>(); //Used to check if after a turn the number of adjacencies has changed

    public Player () {
        scoringToken1 = false;
        scoringToken2 = false;
        score = 0;
        isFirstPlayer = false;
    }

    /*This method will be launched when the game starts once game chooses the first player. it will update isFirstPlayer*/
    public void setAsFirstPlayer() {
        this.isFirstPlayer = true;
    }
    public boolean getIsFirstPlayer(){return isFirstPlayer;}

    /*Sets the player's nickname this method will be invoked by the view and will pass the arg "nickname"*/
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname(){return this.nickname;}

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
    public void updateScore (int score) {this.score += score;}

    /*Each player gets assigned a personal goal card that they have to complete*/
    public void setPersonalGoalCard(PersonalGoalCard pgc) {this.myGoalCard = pgc;}

    /*Returns the player's personal goal card*/
    public PersonalGoalCard getPersonalGoalCard() {
        return this.myGoalCard;
    }

    public boolean getScoringToken1(){return this.scoringToken1;}


    /*Once the player completes a CommonGoalCard, they won't be able to collect other points from that CGC.
    /*This method updates the flag that keeps track of whether the player has already collected points from the first card or not*/
    public void setScoringToken1(){
        this.scoringToken1 = true;
    }

    public boolean getScoringToken2(){ return this.scoringToken2; }

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


    public int checkCompletePGC(){
        Tile[][] bks = myBookshelf.getBookshelf();
        int PGCScore;
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
        /*Based on how many tiles are completed I assign the PGCScore
         *This is good because I don't have to check everytime if
         *I already completed that tile
         */

        switch (numberOfTilesCompleted) {
            case 0 -> PGCScore = 0;
            case 1 -> PGCScore = 1;
            case 2 -> PGCScore = 2;
            case 3 -> PGCScore = 4;
            case 4 -> PGCScore = 6;
            case 5 -> PGCScore = 9;
            case 6 -> PGCScore = 12;
            default -> throw new IllegalStateException("Unexpected value:"+ numberOfTilesCompleted);
        }
        if(PGCScore<=this.scorePGC) {return 0;}
        int deltaScore = PGCScore-this.scorePGC;
        this.scorePGC=PGCScore;
        return deltaScore;
    }


    public int checkAdjacentBookshelf(){
        Tile[][] bookshelf = this.myBookshelf.getBookshelf();
        boolean newCord;
        Cord cord = new Cord();
        Cord nextTo;
        ArrayList<Cord> listOfCords = new ArrayList<>();
        Tile nothing = new Tile(NOTHING,0);
        int i,j;
        int points=0;

        if(previousAdj.isEmpty()) //initialize
            previousAdj.put(new ArrayList<>(),0);

        for (int x=0; x<6; x++) {
            for (int y = 0; y <5; y++) {
                for (int counter = 0; counter < listOfCords.size() || listOfCords.size() == 0; counter++) {
                    newCord = true;
                    if (listOfCords.isEmpty()) {
                        i = x;
                        j = y;
                    } else {
                        i = listOfCords.get(counter).getRowCord();
                        j = listOfCords.get(counter).getColCord();
                    }
                    nextTo = new Cord();
                    nextTo.setCords(i - 1, j);
                    if (i > 0)
                        if (bookshelf[i][j].getType() == bookshelf[i - 1][j].getType() && bookshelf[i][j].getType() != NOTHING) {
                            if (!(listOfCords.contains(cord))) {
                                cord.setCords(i, j);
                                listOfCords.add(cord);
                            }
                            for (Cord listOfCord : listOfCords)
                                if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                                    newCord = false;
                                    break;
                                }
                            if (newCord)
                                listOfCords.add(nextTo);
                        }
                    newCord = true;
                    nextTo = new Cord();
                    nextTo.setCords(i, j + 1);
                    if (j < 4)
                        if (bookshelf[i][j].getType() == bookshelf[i][j + 1].getType() && bookshelf[i][j].getType() != NOTHING) {
                            if (!(listOfCords.contains(cord))) {
                                cord.setCords(i, j);
                                listOfCords.add(cord);
                            }
                            for (Cord listOfCord : listOfCords)
                                if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                                    newCord = false;
                                    break;
                                }
                            if (newCord)
                                listOfCords.add(nextTo);
                        }
                    newCord = true;
                    nextTo = new Cord();
                    nextTo.setCords(i + 1, j);
                    if (i < 5)
                        if (bookshelf[i][j].getType() == bookshelf[i + 1][j].getType() && bookshelf[i][j].getType() != NOTHING) {
                            if (!(listOfCords.contains(cord))) {
                                cord.setCords(i, j);
                                listOfCords.add(cord);
                            }
                            for (Cord listOfCord : listOfCords)
                                if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                                    newCord = false;
                                    break;
                                }
                            if (newCord)
                                listOfCords.add(nextTo);
                        }
                    newCord = true;
                    nextTo = new Cord();
                    nextTo.setCords(i, j - 1);
                    if (j > 0)
                        if (bookshelf[i][j].getType() == bookshelf[i][j - 1].getType() && bookshelf[i][j].getType() != NOTHING) {
                            if (!(listOfCords.contains(cord))) {
                                cord.setCords(i, j);
                                listOfCords.add(cord);
                            }
                            for (Cord listOfCord : listOfCords)
                                if (listOfCord.getRowCord() == nextTo.getRowCord() && listOfCord.getColCord() == nextTo.getColCord()) {
                                    newCord = false;
                                    break;
                                }
                            if (newCord)
                                listOfCords.add(nextTo);
                        }
                    if (listOfCords.size() == 0)
                        break;
                }
                for (int counter = 0; counter < listOfCords.size() && !(listOfCords.isEmpty()); counter++) {
                    int row = listOfCords.get(counter).getRowCord();
                    int col = listOfCords.get(counter).getColCord();
                    bookshelf[row][col] = nothing;
                }
                if (previousAdj.getOrDefault(listOfCords,0)<listOfCords.size()) { //if the amount of ajdacencies has increased then increase the score
                    if (listOfCords.size() == 3)
                        points += 2;
                    if (listOfCords.size() == 4)
                        points += 3;
                    if (listOfCords.size() == 5)
                        points += 5;
                    if (listOfCords.size() >= 6)
                        points += 8;
                    previousAdj.put(listOfCords,listOfCords.size());
                }
                else
                    previousAdj.put(new ArrayList<>(),listOfCords.size());
            }
            listOfCords.clear();
            }
        //p
        System.out.println("Final result: "+points);
        return points;
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


