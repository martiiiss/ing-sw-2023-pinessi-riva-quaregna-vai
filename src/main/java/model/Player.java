package model;

import distributed.messages.Message;
import util.Cord;
import util.Event;
import util.Observable;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import static model.Type.NOTHING;



public class Player extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = 627657924675627426L;
    private String nickname;
    private PersonalGoalCard myGoalCard;
    private boolean isFirstPlayer;
    private int score;
    private Bookshelf myBookshelf;
    private ArrayList<Tile> tilesInHand; //This attribute saves the tiles selected by the player in chooseNTiles so that the bookshelf can be filled with fillBookshelf
    private int scorePGC = 0;  //progressive score of PGC
    int scoreAdj = 0;

    private boolean scoringToken1;
    private boolean scoringToken2;
    // the last two attributes are used to control if the Player has already completed the CGC
    public Player () {
        scoringToken1 = false;
        scoringToken2 = false;
        score = 0;
        isFirstPlayer = false;
    }

    /*This method will be launched when the game starts once game chooses the first player. it will update isFirstPlayer*/
    public void setAsFirstPlayer() {
        this.isFirstPlayer = true;
        notifyObservers(new Message(this, Event.SET_FIRST_PLAYER));
    }
    public boolean getIsFirstPlayer(){return isFirstPlayer;}

    /*Sets the player's nickname this method will be invoked by the view and will pass the arg "nickname"*/
    public void setNickname(String nickname){
        if(nickname.isEmpty() || nickname.equals(null)){
            throw new IllegalArgumentException();
        }
        else {
                this.nickname = nickname;
                notifyObservers(new Message(this, Event.SET_NICKNAME));
        }
    }
    public String getNickname(){return this.nickname;}

    /*Assign to each player their bookshelf*/
    public void setMyBookshelf(Bookshelf bookshelf) {
        this.myBookshelf = bookshelf;
        notifyObservers(new Message(this, Event.SET_PLAYER_BOOKSHELF));

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
    public void updateScore (int score) {this.score += score;
        notifyObservers(new Message(this, Event.UPDATED_SCORE));

    }

    /*Each player gets assigned a personal goal card that they have to complete*/
    public void setPersonalGoalCard(PersonalGoalCard pgc) {this.myGoalCard = pgc;
        notifyObservers(new Message(this, Event.SET_PGC));

    }

    /*Returns the player's personal goal card*/
    public PersonalGoalCard getPersonalGoalCard() {
        return this.myGoalCard;
    }

    public boolean getScoringToken1(){return this.scoringToken1;}


    /*Once the player completes a CommonGoalCard, they won't be able to collect other points from that CGC.
    /*This method updates the flag that keeps track of whether the player has already collected points from the first card or not*/
    public void setScoringToken1(){
        this.scoringToken1 = true;
        notifyObservers(new Message(this, Event.SET_SCORING_TOKEN_1));

    }

    public boolean getScoringToken2(){ return this.scoringToken2; }

    public void setScoringToken2(){
        this.scoringToken2 = true;
        notifyObservers(new Message(this, Event.SET_SCORING_TOKEN_2));

    }

    /*TilesInHand correspond to the tiles that the player wishes to add to their bookshelf*/
    public ArrayList<Tile> getTilesInHand(){
        return this.tilesInHand;
    }

    public void setTilesInHand(ArrayList<Tile> chosenTiles){
        this.tilesInHand = chosenTiles;
        notifyObservers(new Message(this, Event.SET_TILES_IN_HAND));
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
        System.out.println("\u001B[36mScore Before call: "+score);
        System.out.println("\u001B[36mScore Adj Before call: "+scoreAdj+"\u001B[0m");
        score-=scoreAdj;
        scoreAdj=0;
        Tile[][] originalBookshelf = this.myBookshelf.getBookshelf();
        Tile[][] bookshelf = new Tile[6][5];
        for (int x=0; x<6; x++) {
            for (int y = 0; y <5; y++) {
                Type originalType = originalBookshelf[x][y].getType();
                Tile copiedTile = new Tile(originalType,0);
                bookshelf[x][y] = copiedTile;
            }
        }

        boolean newCord;
        Cord cord = new Cord();
        Cord nextTo;
        ArrayList<Cord> listOfCords = new ArrayList<>();
        Tile nothing = new Tile(NOTHING,0);
        int i,j;
        int points=0;

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
                            if (newCord && !listOfCords.contains(nextTo))
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
                            if (newCord && !listOfCords.contains(nextTo))
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
                            if (newCord && !listOfCords.contains(nextTo))
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
                            if (newCord && !listOfCords.contains(nextTo))
                                listOfCords.add(nextTo);
                        }
                    if (listOfCords.size() == 0)
                        break;
                }
            }
            for(Cord cord1 : listOfCords) {
                System.out.print(cord1.getRowCord() + " " + cord1.getColCord()+": ");
                System.out.println(bookshelf[cord1.getRowCord()][cord1.getColCord()].getType());
                bookshelf[cord1.getRowCord()][cord1.getColCord()]=nothing;
            }
            if (listOfCords.size() == 3)
                scoreAdj += 2;
            if (listOfCords.size() == 4)
                scoreAdj += 3;
            if (listOfCords.size() == 5)
                scoreAdj += 5;
            if (listOfCords.size() >= 6)
                scoreAdj += 8;
            System.out.println("\u001B[36mSize of listOfCords: "+listOfCords.size()+" score adj "+scoreAdj+"\u001B[0m");
            listOfCords.clear();
        }
        return scoreAdj;
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

}

